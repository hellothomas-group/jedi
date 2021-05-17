package xyz.hellothomas.jedi.consumer.application;

import xyz.hellothomas.jedi.core.dto.consumer.CustomNotification;
import xyz.hellothomas.jedi.core.dto.consumer.ExecutorShutdownNotification;
import xyz.hellothomas.jedi.core.dto.consumer.ExecutorTaskNotification;
import xyz.hellothomas.jedi.core.dto.consumer.ExecutorTickerNotification;
import xyz.hellothomas.jedi.core.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author Thomas
 * @date 2021/1/30 22:28
 * @description
 * @version 1.0
 */
@Slf4j
@Component
public class KafkaConsumer {
    private final ExecutorTickerService executorTickerService;
    private final ExecutorTaskService executorTaskService;
    private final ExecutorShutdownService executorShutdownService;
    private final CustomMessageService customMessageService;

    public KafkaConsumer(ExecutorTickerService executorTickerService, ExecutorTaskService executorTaskService,
                         ExecutorShutdownService executorShutdownService, CustomMessageService customMessageService) {
        this.executorTickerService = executorTickerService;
        this.executorTaskService = executorTaskService;
        this.executorShutdownService = executorShutdownService;
        this.customMessageService = customMessageService;
    }

    /**
     * 消费监听
     * @param record
     */
    @KafkaListener(topics = "${consumer.executor-ticker-topic}")
    public void onMessageExecutorTicker(ConsumerRecord<?, ?> record) {
        // 消费的哪个topic、partition的消息,打印出消息内容
        log.info("简单消费：{}-{}-{}", record.topic(), record.partition(), record.value());
        ExecutorTickerNotification executorTickerNotification = JsonUtil.deserialize(record.value().toString(),
                ExecutorTickerNotification.class);
        executorTickerService.save(executorTickerNotification);
    }

    /**
     * 消费监听
     * @param record
     */
    @KafkaListener(topics = "${consumer.executor-task-topic}")
    public void onMessageExecutorTask(ConsumerRecord<?, ?> record) {
        // 消费的哪个topic、partition的消息,打印出消息内容
        log.info("简单消费：{}-{}-{}", record.topic(), record.partition(), record.value());
        ExecutorTaskNotification executorTaskNotification = JsonUtil.deserialize(record.value().toString(),
                ExecutorTaskNotification.class);
        executorTaskService.save(executorTaskNotification);
    }

    /**
     * 消费监听
     * @param record
     */
    @KafkaListener(topics = "${consumer.executor-shutdown-topic}")
    public void onMessageExecutorShutdown(ConsumerRecord<?, ?> record) {
        // 消费的哪个topic、partition的消息,打印出消息内容
        log.info("简单消费：{}-{}-{}", record.topic(), record.partition(), record.value());
        ExecutorShutdownNotification executorShutdownNotification = JsonUtil.deserialize(record.value().toString(),
                ExecutorShutdownNotification.class);
        executorShutdownService.save(executorShutdownNotification);
    }

    /**
     * 消费监听
     * @param record
     */
    @KafkaListener(topics = "${consumer.custom-notification-topic}")
    public void onMessageCustomNotification(ConsumerRecord<?, ?> record) {
        // 消费的哪个topic、partition的消息,打印出消息内容
        log.info("简单消费：{}-{}-{}", record.topic(), record.partition(), record.value());
        CustomNotification customNotification = JsonUtil.deserialize(record.value().toString(),
                CustomNotification.class);
        customMessageService.save(customNotification);
    }
}
