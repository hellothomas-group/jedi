package com.hellothomas.jedi.config.infrastructure.config;

import com.hellothomas.jedi.config.infrastructure.aop.LoggingAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(name = "config-service.aspect.logging.enable", havingValue = "true")
@Configuration
public class LoggingAspectConfig {

    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
