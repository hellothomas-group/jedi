package xyz.hellothomas.jedi.consumer.infrastructure.config;

import xyz.hellothomas.jedi.consumer.infrastructure.aop.LoggingAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingAspectConfig {

    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
