package xyz.hellothomas.jedi.client.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.hellothomas.jedi.client.constants.Constants;
import xyz.hellothomas.jedi.client.trace.AsyncTraceFactory;
import xyz.hellothomas.jedi.client.trace.DefaultAsyncTraceFactory;

/**
 * @author Thomas
 * @date 2021/12/12 10:18
 * @description
 * @version 1.0
 */
@ConditionalOnProperty(value = Constants.JEDI_CONFIG_ENABLE_KEY, havingValue = "true")
@Configuration
public class AsyncTraceConfig {

    @Bean
    @ConditionalOnMissingBean(AsyncTraceFactory.class)
    public AsyncTraceFactory asyncTraceFactory() {
        return new DefaultAsyncTraceFactory();
    }
}
