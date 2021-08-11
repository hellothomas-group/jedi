package xyz.hellothomas.jedi.consumer.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 80234613 唐圆
 * @date 2021/8/11 14:26
 * @descripton
 * @version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "consumer")
public class ConsumerLocalProperties {
    private String type;

    private String url;

    private String executorTickerTopic;

    private String executorTaskTopic;

    private String executorShutdownTopic;

    private String customNotificationTopic;
}
