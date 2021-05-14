package com.hellothomas.jedi.admin.infrastructure.config;

import com.hellothomas.jedi.admin.infrastructure.aop.LoggingAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(name = "admin-service.aspect.logging.enable", havingValue = "true")
@Configuration
public class LoggingAspectConfig {

    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
