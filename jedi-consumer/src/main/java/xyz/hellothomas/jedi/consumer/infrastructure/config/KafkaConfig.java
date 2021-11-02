package xyz.hellothomas.jedi.consumer.infrastructure.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.hellothomas.jedi.consumer.application.*;

/**
 * @author Thomas
 * @date 2021/6/23 22:22
 * @description
 * @version 1.0
 */
@ConditionalOnProperty(name = "consumer.type", havingValue = "KAFKA")
@Configuration
public class KafkaConfig {

    @Bean
    public KafkaConsumer kafkaConsumer(ExecutorTickerMsgService executorTickerMsgService,
                                       ExecutorTaskMsgService executorTaskMsgService,
                                       ExecutorShutdownMsgService executorShutdownMsgService,
                                       CustomMsgService customMsgService) {
        return new KafkaConsumer(executorTickerMsgService, executorTaskMsgService, executorShutdownMsgService,
                customMsgService);
    }
}
