package xyz.hellothomas.jedi.client.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.hellothomas.jedi.client.aop.JediAsyncAspect;
import xyz.hellothomas.jedi.client.constants.Constants;
import xyz.hellothomas.jedi.client.persistence.JdbcTemplatePersistenceService;
import xyz.hellothomas.jedi.client.persistence.NullPersistentService;
import xyz.hellothomas.jedi.client.persistence.PersistenceService;
import xyz.hellothomas.jedi.core.trace.AsyncTraceFactory;
import xyz.hellothomas.jedi.core.trace.DefaultAsyncTraceFactory;

@ConditionalOnProperty(value = Constants.JEDI_CONFIG_ENABLE_KEY, havingValue = "true")
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
    @ConditionalOnClass(name = "org.springframework.jdbc.core.JdbcTemplate")
    @ConditionalOnMissingBean
    public PersistenceService persistenceService() {
        return new JdbcTemplatePersistenceService();
    }

    @Bean
    @ConditionalOnMissingBean
    public PersistenceService nullPersistentService() {
        return new NullPersistentService();
    }
}
