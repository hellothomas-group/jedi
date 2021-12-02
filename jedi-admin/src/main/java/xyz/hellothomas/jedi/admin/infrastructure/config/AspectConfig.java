package xyz.hellothomas.jedi.admin.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.hellothomas.jedi.admin.infrastructure.aop.AuthorizeAspect;
import xyz.hellothomas.jedi.admin.infrastructure.aop.LoggingAspect;

@Slf4j
@Configuration
public class AspectConfig {

    @Bean
    public AuthorizeAspect authorizeAspect() {
        return new AuthorizeAspect();
    }

    @Bean
    @ConditionalOnProperty(name = "admin-service.aspect.logging.enable", havingValue = "true")
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
