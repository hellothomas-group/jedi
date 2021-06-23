package xyz.hellothomas.jedi.consumer.infrastructure.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import xyz.hellothomas.jedi.consumer.infrastructure.aop.LoggingAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(name = "consumer-service.aspect.logging.enable", havingValue = "true")
@Configuration
public class LoggingAspectConfig {

    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
