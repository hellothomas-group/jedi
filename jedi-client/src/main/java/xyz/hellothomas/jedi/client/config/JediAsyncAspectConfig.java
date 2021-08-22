package xyz.hellothomas.jedi.client.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.hellothomas.jedi.client.aop.JediAsyncAspect;
import xyz.hellothomas.jedi.client.constants.Constants;

@ConditionalOnProperty(value = Constants.JEDI_CONFIG_ENABLE_KEY, havingValue = "true")
@Configuration
public class JediAsyncAspectConfig {

    @Bean
    public JediAsyncAspect loggingAspect() {
        return new JediAsyncAspect();
    }
}
