package com.hellothomas.jedi.client.config;

import com.hellothomas.jedi.client.constants.Constants;
import com.hellothomas.jedi.client.internals.PropertySourcesProcessor;
import com.hellothomas.jedi.core.internals.executor.DynamicThreadPoolExecutor;
import com.hellothomas.jedi.core.internals.executor.DynamicThreadPoolProperty;
import com.hellothomas.jedi.core.internals.message.AbstractNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.concurrent.Executor;


/**
 * @className ExecutorConfig
 * @author Thomas
 * @date 2021/4/11 13:37
 * @description
 * @version 1.0
 */
@Slf4j
@Configuration
@Import(ExecutorRegistrar.class)
@ConditionalOnProperty(value = "monitor.enable", havingValue = "true")
public class ExecutorConfig {

    @Bean(name = {Constants.DEFAULT_EXECUTOR_NAME})
    public Executor defaultExecutor(MonitorConfig monitorConfig, AbstractNotificationService notificationService) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        DynamicThreadPoolProperty dynamicThreadPoolProperty =
                monitorConfig.getExecutors().stream()
                        .filter(i -> i != null && Constants.DEFAULT_EXECUTOR_NAME.equals(i.getName()))
                        .findFirst()
                        .orElse(DynamicThreadPoolProperty.builder()
                                .name(Constants.DEFAULT_EXECUTOR_NAME)
                                .build());
        dynamicThreadPoolProperty.setNotificationService(notificationService);
        log.debug("{}配置为:{}", Constants.DEFAULT_EXECUTOR_NAME, dynamicThreadPoolProperty);

        return new DynamicThreadPoolExecutor(dynamicThreadPoolProperty);
    }

    @Bean
    public PropertySourcesProcessor propertySourcesProcessor() {
        return new PropertySourcesProcessor();
    }
}
