package xyz.hellothomas.jedi.client.config;

import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import xyz.hellothomas.jedi.client.Config;
import xyz.hellothomas.jedi.client.ConfigService;
import xyz.hellothomas.jedi.client.constants.Constants;
import xyz.hellothomas.jedi.client.internals.ConfigPropertySourceFactory;

import java.util.List;

/**
 * @author Thomas
 * @date 2021/4/19 22:31
 * @description
 * @version 1.0
 */
@Slf4j
@Order(value = 0)
public class MonitorApplicationInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>,
        EnvironmentPostProcessor {
    private static final String[] JEDI_SYSTEM_PROPERTIES = {Constants.MONITOR_CONFIG_ENABLE_KEY, Constants.MONITOR_CONFIG_URL_KEY,
            Constants.MONITOR_CONFIG_NAMESPACE_KEY, Constants.MONITOR_CONFIG_APP_ID_KEY};
    private static final Splitter EXECUTOR_SPLITTER = Splitter.on(",").omitEmptyStrings().trimResults();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Boolean monitorEnable = environment.getProperty(Constants.MONITOR_CONFIG_ENABLE_KEY, Boolean.class, false);

        if (!monitorEnable) {
            return;
        }

        // should always initialize system properties like monitor.app-id in the first place
        initializeSystemProperty(environment);
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Boolean monitorEnable = applicationContext.getEnvironment().getProperty(Constants.MONITOR_CONFIG_ENABLE_KEY,
                Boolean.class, false);

        if (!monitorEnable) {
            return;
        }

        initializeConfig(applicationContext.getEnvironment());
    }

    /**
     * To fill system properties from environment config
     */
    private void initializeSystemProperty(ConfigurableEnvironment environment) {
        for (String propertyName : JEDI_SYSTEM_PROPERTIES) {
            fillSystemPropertyFromEnvironment(environment, propertyName);
        }
    }

    private void fillSystemPropertyFromEnvironment(ConfigurableEnvironment environment, String propertyName) {
        if (System.getProperty(propertyName) != null) {
            return;
        }

        String propertyValue = environment.getProperty(propertyName);

        if (StringUtils.isBlank(propertyValue)) {
            return;
        }

        System.setProperty(propertyName, propertyValue);
    }

    /**
     * Initialize Jedi Configurations Just after environment is ready.
     *
     * @param environment
     */
    private void initializeConfig(ConfigurableEnvironment environment) {
        String executors = environment.getProperty(Constants.MONITOR_CONFIG_EXECUTORS_KEY);
        if (StringUtils.isBlank(executors)) {
            log.warn("未配置{}, 将使用默认executor:{}", Constants.MONITOR_CONFIG_EXECUTORS_KEY, Constants.DEFAULT_EXECUTOR_NAME);
            return;
        }

        log.debug("Jedi bootstrap executors: {}", executors);
        List<String> executorNames = EXECUTOR_SPLITTER.splitToList(executors);

        CompositePropertySource composite =
                new CompositePropertySource(Constants.JEDI_BOOTSTRAP_PROPERTY_SOURCE_NAME);

        for (String executor : executorNames) {
            Config config = ConfigService.getConfig(executor);

            composite.addPropertySource(ConfigPropertySourceFactory.getConfigPropertySource(executor, config));
        }

        environment.getPropertySources().addFirst(composite);
    }
}
