package xyz.hellothomas.jedi.collector.infrastructure.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import xyz.hellothomas.jedi.collector.infrastructure.aop.LoggingAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(name = "collector-service.aspect.logging.enable", havingValue = "true")
@Configuration
public class LoggingAspectConfig {

    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
