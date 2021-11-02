package xyz.hellothomas.jedi.consumer.application;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import xyz.hellothomas.jedi.core.dto.consumer.CustomNotification;
import xyz.hellothomas.jedi.core.dto.consumer.ExecutorShutdownNotification;
import xyz.hellothomas.jedi.core.dto.consumer.ExecutorTaskNotification;
import xyz.hellothomas.jedi.core.dto.consumer.ExecutorTickerNotification;
import xyz.hellothomas.jedi.core.utils.JsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Thomas
 * @date 2021/1/30 22:28
 * @description
 * @version 1.0
 */
@Slf4j
public class KafkaConsumer {
    private final ExecutorTickerMsgService executorTickerMsgService;
    private final ExecutorTaskMsgService executorTaskMsgService;
    private final ExecutorShutdownMsgService executorShutdownMsgService;
    private final CustomMsgService customMsgService;

    public KafkaConsumer(ExecutorTickerMsgService executorTickerMsgService, ExecutorTaskMsgService executorTaskMsgService,
                         ExecutorShutdownMsgService executorShutdownMsgService, CustomMsgService customMsgService) {
        this.executorTickerMsgService = executorTickerMsgService;
        this.executorTaskMsgService = executorTaskMsgService;
        this.executorShutdownMsgService = executorShutdownMsgService;
        this.customMsgService = customMsgService;
    }

    /**
     * 消费监听
     * @param records
     */
    @KafkaListener(topics = "${consumer.executor-ticker-topic}")
    public void onMessageExecutorTicker(List<ConsumerRecord<?, ?>> records) {
        // todo manual-commit
        // 消费的哪个topic、partition的消息,打印出消息内容
        List notifications = new ArrayList(records.size());
        records.stream().forEach(i -> {
            log.debug("ticker消费：{}-{}-{}", i.topic(), i.partition(), i.value());
            ExecutorTickerNotification executorTickerNotification = JsonUtil.deserialize(i.value().toString(),
                    ExecutorTickerNotification.class);
            notifications.add(executorTickerNotification);
        });
        executorTickerMsgService.process(notifications);
    }

    /**
     * 消费监听
     * @param records
     */
    @KafkaListener(topics = "${consumer.executor-task-topic}")
    public void onMessageExecutorTask(List<ConsumerRecord<?, ?>> records) {
        // 消费的哪个topic、partition的消息,打印出消息内容
        List notifications = new ArrayList(records.size());
        records.stream().forEach(i -> {
            log.debug("task消费：{}-{}-{}", i.topic(), i.partition(), i.value());
            ExecutorTaskNotification executorTaskNotification = JsonUtil.deserialize(i.value().toString(),
                    ExecutorTaskNotification.class);
            notifications.add(executorTaskNotification);
        });
        executorTaskMsgService.process(notifications);
    }

    /**
     * 消费监听
     * @param records
     */
    @KafkaListener(topics = "${consumer.executor-shutdown-topic}")
    public void onMessageExecutorShutdown(List<ConsumerRecord<?, ?>> records) {
        // 消费的哪个topic、partition的消息,打印出消息内容
        records.stream().forEach(i -> {
            log.debug("shutdown消费：{}-{}-{}", i.topic(), i.partition(), i.value());
            ExecutorShutdownNotification executorShutdownNotification = JsonUtil.deserialize(i.value().toString(),
                    ExecutorShutdownNotification.class);
            executorShutdownMsgService.process(executorShutdownNotification);
        });
    }

    /**
     * 消费监听
     * @param records
     */
    @KafkaListener(topics = "${consumer.custom-notification-topic}")
    public void onMessageCustomNotification(List<ConsumerRecord<?, ?>> records) {
        // 消费的哪个topic、partition的消息,打印出消息内容
        records.stream().forEach(i -> {
            log.debug("custom消费：{}-{}-{}", i.topic(), i.partition(), i.value());
            CustomNotification customNotification = JsonUtil.deserialize(i.value().toString(),
                    CustomNotification.class);
            customMsgService.process(customNotification);
        });
    }
}
