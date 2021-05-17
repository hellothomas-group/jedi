package xyz.hellothomas.jedi.client.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import xyz.hellothomas.jedi.client.constants.Constants;
import xyz.hellothomas.jedi.client.internals.PropertySourcesProcessor;
import xyz.hellothomas.jedi.core.internals.executor.JediThreadPoolExecutor;
import xyz.hellothomas.jedi.core.internals.executor.JediThreadPoolProperty;
import xyz.hellothomas.jedi.core.internals.message.AbstractNotificationService;

import java.util.concurrent.Executor;


/**
 * @author Thomas
 * @date 2021/4/11 13:37
 * @description
 * @version 1.0
 */
@Slf4j
@Configuration
@Import(JediExecutorRegistrar.class)
@ConditionalOnProperty(value = "monitor.enable", havingValue = "true")
public class JediExecutorAutoConfig {

    @Bean(name = {Constants.DEFAULT_EXECUTOR_NAME})
    public Executor defaultExecutor(JediConfig jediConfig, AbstractNotificationService notificationService) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        JediThreadPoolProperty jediThreadPoolProperty =
                jediConfig.getExecutors().stream()
                        .filter(i -> i != null && Constants.DEFAULT_EXECUTOR_NAME.equals(i.getName()))
                        .findFirst()
                        .orElse(JediThreadPoolProperty.builder()
                                .name(Constants.DEFAULT_EXECUTOR_NAME)
                                .build());
        jediThreadPoolProperty.setNotificationService(notificationService);
        log.debug("{}配置为:{}", Constants.DEFAULT_EXECUTOR_NAME, jediThreadPoolProperty);

        return new JediThreadPoolExecutor(jediThreadPoolProperty);
    }

    @Bean
    public PropertySourcesProcessor propertySourcesProcessor() {
        return new PropertySourcesProcessor();
    }
}
