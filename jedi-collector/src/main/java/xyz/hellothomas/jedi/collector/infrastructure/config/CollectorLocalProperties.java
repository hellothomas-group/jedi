package xyz.hellothomas.jedi.collector.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Thomas
 * @date 2021/8/11 14:26
 * @descripton
 * @version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "collector")
public class CollectorLocalProperties {
    private String type;

    private String url;

    private String executorTickerTopic;

    private String executorTaskTopic;

    private String executorShutdownTopic;

    private String customNotificationTopic;
}
