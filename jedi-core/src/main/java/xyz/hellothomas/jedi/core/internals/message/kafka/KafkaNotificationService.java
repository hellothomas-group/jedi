package xyz.hellothomas.jedi.core.internals.message.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.hellothomas.jedi.core.dto.consumer.AbstractNotification;
import xyz.hellothomas.jedi.core.enums.KafkaMessageTopic;
import xyz.hellothomas.jedi.core.enums.MessageType;
import xyz.hellothomas.jedi.core.internals.message.AbstractNotificationService;
import xyz.hellothomas.jedi.core.schedule.ExponentialSchedulePolicy;
import xyz.hellothomas.jedi.core.schedule.SchedulePolicy;
import xyz.hellothomas.jedi.core.utils.JsonUtil;
import xyz.hellothomas.jedi.core.utils.SleepUtil;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Thomas
 * @date 2021/1/24 10:15
 * @description
 * @version 1.0
 */
public class KafkaNotificationService extends AbstractNotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaNotificationService.class);

    /**
     * 缓冲消息队列
     */
    private LinkedBlockingQueue<AbstractNotification> notificationQueue;

    /**
     * 缓冲消息发送失败后补偿队列
     */
    private LinkedBlockingQueue<AbstractNotification> failSendQueue;

    private SchedulePolicy sendFailbackSchedulePolicyInSecond;

    /**
     * 消息发送线程终止标志
     */
    private volatile boolean toStop;

    /**
     * 消息发送失败后补偿线程终止标志
     */
    private volatile boolean toStopFailback;

    private KafkaProperty kafkaProperty;

    private LazyProducer lazyProducer = null;

    private LazyProducer lazyProducerFailback = null;

    public KafkaNotificationService(KafkaProperty kafkaProperty, String appId, String namespace) {
        super(appId, namespace);
        this.kafkaProperty = kafkaProperty;
        this.notificationQueue = new LinkedBlockingQueue<>(5000);
        this.failSendQueue = new LinkedBlockingQueue<>(1000);
        this.sendFailbackSchedulePolicyInSecond = new ExponentialSchedulePolicy(1, 300);
        start();
        startMessageSendThread();
        startMessageSendFailbackThread();
    }

    @Override
    public void pushNotification(AbstractNotification notification) {
        try {
            notificationQueue.add(notification);
        } catch (Exception e) {
            LOGGER.warn("消息{} push异常: {}", notification, e);
        }
    }

    @Override
    public void send(Object request, MessageType messageType) {
        String topic = getMessageTopic(messageType);
        final ProducerRecord<String, String> producerRecord = new ProducerRecord(topic, JsonUtil.serialize(request));
        lazyProducer.get().send(producerRecord, ((metadata, exception) -> {
            if (exception == null) {
                LOGGER.trace("Success sent message: {}, metadata: {}", producerRecord, metadata);
            } else {
                LOGGER.warn(String.format("Fail send message: %s, metadata: %s", producerRecord, metadata), exception);
                // 任务消息避免丢失
                if (MessageType.EXECUTOR_TASK == messageType) {
                    pushFailMessage((AbstractNotification) request);
                }
            }
        }));
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
        // only error free appenders should be activated
        if (!kafkaProperty.checkPrerequisites()) {
            return;
        }

        if (kafkaProperty.getPartition() != null && kafkaProperty.getPartition() < 0) {
            kafkaProperty.setPartition(null);
        }

        lazyProducer = new LazyProducer();
        lazyProducerFailback = new LazyProducer();
    }

    private String getMessageTopic(MessageType messageType) {
        String topic = kafkaProperty.getTopics().get(messageType);
        if (topic == null) {
            topic = KafkaMessageTopic.getTopicByMessageType(messageType);
        }
        if (topic == null) {
            topic = kafkaProperty.getDefaultTopic();
        }
        return topic;
    }

    /**
     * 启动守护线程执行消息发送任务
     *
     * @author 80234613 唐圆
     * @date 2019-11-14
     * @param
     * @return void
     */
    private void startMessageSendThread() {
        Thread messageSendThread = new Thread(() -> {
            while (!toStop) {
                try {
                    LOGGER.trace("获取消息发送队列中...");
                    AbstractNotification notification = notificationQueue.take();
                    if (MessageType.EXECUTOR_TASK.getTypeValue().equals(notification.getMessageType())) {
                        send(notification, MessageType.EXECUTOR_TASK);
                    } else if (MessageType.EXECUTOR_TICKER.getTypeValue().equals(notification.getMessageType())) {
                        send(notification, MessageType.EXECUTOR_TICKER);
                    } else if (MessageType.EXECUTOR_SHUTDOWN.getTypeValue().equals(notification.getMessageType())) {
                        send(notification, MessageType.EXECUTOR_SHUTDOWN);
                    } else {
                        send(notification, MessageType.CUSTOM_NOTIFICATION);
                    }
                } catch (Exception e) {
                    if (!toStop) {
                        LOGGER.error("消息发送任务失败, 异常为: {}, 消息为: {}! ", e, notificationQueue);
                    }
                }
            }
        });

        messageSendThread.setDaemon(true);
        messageSendThread.setName("消息发送线程");
        messageSendThread.start();
        LOGGER.debug("{}已启动", messageSendThread.getName());
    }

    private void pushFailMessage(AbstractNotification notification) {
        try {
            failSendQueue.add(notification);
        } catch (Exception e) {
            LOGGER.warn("失败消息{} push异常: {}", notification, e);
        }
    }

    private void sendFailMessage(Object request, MessageType messageType) throws ExecutionException,
            InterruptedException {
        String topic = getMessageTopic(messageType);
        final ProducerRecord<String, String> producerRecord = new ProducerRecord(topic, JsonUtil.serialize(request));
        lazyProducerFailback.get().send(producerRecord).get();
    }

    /**
     * 启动守护线程执行发送失败消息补偿任务
     *
     * @author 80234613 唐圆
     * @date 2019-11-14
     * @param
     * @return void
     */
    private void startMessageSendFailbackThread() {
        Thread messageSendFailbackThread = new Thread(() -> {
            while (!toStopFailback) {
                AbstractNotification notification = null;
                try {
                    LOGGER.trace("获取发送失败消息队列中...");
                    notification = failSendQueue.take();
                    if (MessageType.EXECUTOR_TASK.getTypeValue().equals(notification.getMessageType())) {
                        sendFailMessage(notification, MessageType.EXECUTOR_TASK);
                    } else if (MessageType.EXECUTOR_TICKER.getTypeValue().equals(notification.getMessageType())) {
                        sendFailMessage(notification, MessageType.EXECUTOR_TICKER);
                    } else if (MessageType.EXECUTOR_SHUTDOWN.getTypeValue().equals(notification.getMessageType())) {
                        sendFailMessage(notification, MessageType.EXECUTOR_SHUTDOWN);
                    } else {
                        sendFailMessage(notification, MessageType.CUSTOM_NOTIFICATION);
                    }
                    sendFailbackSchedulePolicyInSecond.success();
                } catch (Exception e) {
                    if (!toStopFailback) {
                        LOGGER.error("失败消息补偿发送任务失败, 异常为: {}, 消息为: {}! ", e, notificationQueue);
                        pushFailMessage(notification);
                        long sleepTimeInSecond = sendFailbackSchedulePolicyInSecond.fail();
                        SleepUtil.sleepInSecond(sleepTimeInSecond);
                    }
                }
            }
        });

        messageSendFailbackThread.setDaemon(true);
        messageSendFailbackThread.setName("失败消息补偿发送线程");
        messageSendFailbackThread.start();
        LOGGER.debug("{}已启动", messageSendFailbackThread.getName());
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
