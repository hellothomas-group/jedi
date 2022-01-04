package xyz.hellothomas.jedi.client.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import xyz.hellothomas.jedi.client.aop.JediAsyncAspect;
import xyz.hellothomas.jedi.client.constants.Constants;
import xyz.hellothomas.jedi.client.model.JediProperty;
import xyz.hellothomas.jedi.client.persistence.NullPersistentService;
import xyz.hellothomas.jedi.client.persistence.PersistenceService;
import xyz.hellothomas.jedi.core.trace.AsyncTraceFactory;
import xyz.hellothomas.jedi.core.trace.DefaultAsyncTraceFactory;

@EnableConfigurationProperties(JediProperty.class)
@ConditionalOnProperty(value = Constants.JEDI_CONFIG_ENABLE_KEY, havingValue = "true")
@Import(JediPersistenceConfig.class)
@Configuration
public class JediAsyncAutoConfig {

    @Bean
    public JediAsyncAspect jediAsyncAspect() {
        return new JediAsyncAspect();
    }

    @Bean
    @ConditionalOnMissingBean(AsyncTraceFactory.class)
    public AsyncTraceFactory defaultAsyncTraceFactory() {
        return new DefaultAsyncTraceFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public PersistenceService nullPersistentService() {
        return new NullPersistentService();
    }
}
