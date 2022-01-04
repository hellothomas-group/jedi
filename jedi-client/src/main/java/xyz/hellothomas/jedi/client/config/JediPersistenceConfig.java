package xyz.hellothomas.jedi.client.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.hellothomas.jedi.client.internals.DefaultRecoverTaskService;
import xyz.hellothomas.jedi.client.internals.DefaultRetryTaskService;
import xyz.hellothomas.jedi.client.internals.RecoverTaskService;
import xyz.hellothomas.jedi.client.internals.RetryTaskService;
import xyz.hellothomas.jedi.client.model.JediProperty;
import xyz.hellothomas.jedi.client.persistence.JdbcTemplatePersistenceService;
import xyz.hellothomas.jedi.client.persistence.PersistenceService;

/**
 * @author Thomas
 * @date 2022/1/3 12:01
 * @description
 * @version 1.0
 */
@ConditionalOnProperty(prefix = "jedi.persistence", name = "enable", havingValue = "true")
@Configuration
public class JediPersistenceConfig {

    @Bean
    @ConditionalOnClass(name = "org.springframework.jdbc.core.JdbcTemplate")
    @ConditionalOnMissingBean
    public PersistenceService jdbcTemplatePersistenceService() {
        return new JdbcTemplatePersistenceService();
    }


    @Bean
    @ConditionalOnProperty(prefix = "jedi.persistence.retryer", name = "enable", havingValue = "true", matchIfMissing =
            true)
    @ConditionalOnClass(name = "org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping")
    @ConditionalOnMissingBean
    public RetryTaskService retryTaskService(PersistenceService persistenceService, JediProperty jediProperty) {
        return new DefaultRetryTaskService(persistenceService, jediProperty);
    }

    @Bean
    @ConditionalOnProperty(prefix = "jedi.persistence.recover", name = "enable", havingValue = "true", matchIfMissing =
            true)
    @ConditionalOnMissingBean
    public RecoverTaskService recoverTaskService(PersistenceService persistenceService, JediProperty jediProperty) {
        return new DefaultRecoverTaskService(persistenceService, jediProperty);
    }
}
