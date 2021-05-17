package xyz.hellothomas.jedi.admin.infrastructure.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.hellothomas.jedi.admin.infrastructure.aop.LoggingAspect;

@ConditionalOnProperty(name = "admin-service.aspect.logging.enable", havingValue = "true")
@Configuration
public class LoggingAspectConfig {

    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
