package xyz.hellothomas.jedi.core.internals.message.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.hellothomas.jedi.core.dto.collector.AbstractNotification;
import xyz.hellothomas.jedi.core.enums.KafkaMessageTopic;
import xyz.hellothomas.jedi.core.enums.MessageType;
import xyz.hellothomas.jedi.core.internals.message.AbstractNotificationService;
import xyz.hellothomas.jedi.core.schedule.ExponentialSchedulePolicy;
import xyz.hellothomas.jedi.core.schedule.SchedulePolicy;
import xyz.hellothomas.jedi.core.utils.JediThreadFactory;
import xyz.hellothomas.jedi.core.utils.JsonUtil;
import xyz.hellothomas.jedi.core.utils.SleepUtil;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Thomas
 * @date 2021/1/24 10:15
 * @description
 * @version 1.0
 */
public class KafkaNotificationService extends AbstractNotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaNotificationService.class);

    private ThreadPoolExecutor sendNotificationExecutor;

    private ThreadPoolExecutor retrySendNotificationExecutor;

    private SchedulePolicy retrySendSchedulePolicyInSecond;

    private KafkaProperty kafkaProperty;

    private LazyProducer lazyProducer = null;

    private LazyProducer lazyRetryProducer = null;

    public KafkaNotificationService(KafkaProperty kafkaProperty, String appId, String namespace) {
        super(appId, namespace);
        this.kafkaProperty = kafkaProperty;
        this.sendNotificationExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(5000), JediThreadFactory.create("kafka-send", false));
        this.retrySendNotificationExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000), JediThreadFactory.create("kafka-retry-send", false));
        this.retrySendSchedulePolicyInSecond = new ExponentialSchedulePolicy(1, 300);
        start();
    }

    @Override
    public void pushNotification(AbstractNotification notification) {
        if (lazyProducer == null) {
            return;
        }

        try {
            sendNotificationExecutor.execute(() -> {
                MessageType messageType = MessageType.getMessageType(notification.getMessageType());
                send(notification, messageType);
            });
        } catch (Exception e) {
            LOGGER.warn("??????{} push??????: {}", notification, e);
        }
    }

    @Override
    public void send(Object notification, MessageType messageType) {
        try {
            String topic = getMessageTopic(messageType);
            final ProducerRecord<String, String> producerRecord = new ProducerRecord(topic,
                    JsonUtil.serialize(notification));
            lazyProducer.get().send(producerRecord, ((metadata, exception) -> {
                if (exception == null) {
                    LOGGER.trace("Success sent message: {}, metadata: {}", producerRecord, metadata);
                } else {
                    LOGGER.warn(String.format("Fail send message: %s, metadata: %s", producerRecord, metadata),
                            exception);
                    // ????????????????????????
                    if (MessageType.EXECUTOR_TASK == messageType) {
                        pushFailMessage((AbstractNotification) notification, messageType);
                    }
                }
            }));
        } catch (Exception e) {
            LOGGER.warn("????????????????????????, ?????????: {}, ?????????: {}! ", e, notification);
        }
    }

    public void stop() {
        if (lazyProducer != null && lazyProducer.isInitialized()) {
            try {
                lazyProducer.get().close();
            } catch (KafkaException e) {
                LOGGER.error("Failed to shut down kafka producer: " + e.getMessage(), e);
            }
            lazyProducer = null;
        }
    }

    private void start() {
        if (!kafkaProperty.checkPrerequisites()) {
            return;
        }

        if (kafkaProperty.getPartition() != null && kafkaProperty.getPartition() < 0) {
            kafkaProperty.setPartition(null);
        }

        lazyProducer = new LazyProducer();
        lazyRetryProducer = new LazyProducer();
    }

    private String getMessageTopic(MessageType messageType) {
        if (messageType == null) {
            return kafkaProperty.getDefaultTopic();
        }

        String topic = kafkaProperty.getTopics().get(messageType);
        if (topic == null) {
            topic = KafkaMessageTopic.getTopicByMessageType(messageType);
        }
        if (topic == null) {
            topic = kafkaProperty.getDefaultTopic();
        }
        return topic;
    }

    private void pushFailMessage(AbstractNotification notification, MessageType messageType) {
        try {
            retrySendNotificationExecutor.execute(() -> retrySendMessage(notification, messageType));
        } catch (Exception e) {
            LOGGER.warn("????????????{} push??????: {}", notification, e);
        }
    }

    private void retrySendMessage(AbstractNotification notification, MessageType messageType) {
        try {
            String topic = getMessageTopic(messageType);
            final ProducerRecord<String, String> producerRecord = new ProducerRecord(topic,
                    JsonUtil.serialize(notification));
            lazyRetryProducer.get().send(producerRecord).get();
            retrySendSchedulePolicyInSecond.success();
        } catch (Exception e) {
            LOGGER.warn("????????????????????????????????????, ?????????: {}, ?????????: {}! ", e, notification);
            pushFailMessage(notification, messageType);
            long sleepTimeInSecond = retrySendSchedulePolicyInSecond.fail();
            SleepUtil.sleepInSecond(sleepTimeInSecond);
        }
    }

    private class LazyProducer {
        private volatile Producer<String, String> producer;

        public Producer<String, String> get() {
            Producer<String, String> result = this.producer;
            if (result == null) {
                synchronized (this) {
                    result = this.producer;
                    if (result == null) {
                        this.producer = result = this.initialize();
                    }
                }
            }

            return result;
        }

        public boolean isInitialized() {
            return producer != null;
        }

        private Producer<String, String> initialize() {
            Producer<String, String> newProducer = null;
            try {
                newProducer = createProducer();
            } catch (Exception e) {
                LOGGER.error("error creating producer", e);
            }
            return newProducer;
        }

        private Producer<String, String> createProducer() {
            return new KafkaProducer<>(new HashMap<>(KafkaNotificationService.this.kafkaProperty.getProducerConfig()));
        }
    }
}
