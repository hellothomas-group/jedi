package xyz.hellothomas.jedi.config.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Thomas
 * @date 2021/4/5 11:43
 * @description
 * @version 1.0
 */
@Component
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "config-service")
public class ConfigServiceProperty {
    private boolean cacheEnabled;
}
