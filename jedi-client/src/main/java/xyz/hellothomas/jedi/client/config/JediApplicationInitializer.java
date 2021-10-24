package xyz.hellothomas.jedi.client.config;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
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
import xyz.hellothomas.jedi.client.enums.JediModeEnum;
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
public class JediApplicationInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>,
        EnvironmentPostProcessor {
    private static final String[] JEDI_SYSTEM_PROPERTIES = {Constants.JEDI_CONFIG_ENABLE_KEY,
            Constants.JEDI_CONFIG_MODE_KEY,
            Constants.JEDI_CONFIG_URL_KEY,
            Constants.JEDI_CONFIG_NAMESPACE_KEY,
            Constants.JEDI_CONFIG_APP_ID_KEY,
            Constants.JEDI_CONFIG_ORDER_KEY};
    private static final Splitter EXECUTOR_SPLITTER = Splitter.on(",").omitEmptyStrings().trimResults();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Boolean jediEnable = environment.getProperty(Constants.JEDI_CONFIG_ENABLE_KEY, Boolean.class, false);

        if (!jediEnable) {
            return;
        }

        Integer jediMode = environment.getProperty(Constants.JEDI_CONFIG_MODE_KEY, Integer.class,
                JediModeEnum.DEFAULT.getEnumValue());

        if (JediModeEnum.OFFLINE.getEnumValue() == jediMode) {
            return;
        }

        // should always initialize system properties like jedi.app-id in the first place
        initializeSystemProperty(environment);
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Boolean jediEnable = applicationContext.getEnvironment().getProperty(Constants.JEDI_CONFIG_ENABLE_KEY,
                Boolean.class, false);

        if (!jediEnable) {
            return;
        }

        int jediMode = applicationContext.getEnvironment().getProperty(Constants.JEDI_CONFIG_MODE_KEY,
                Integer.class, JediModeEnum.DEFAULT.getEnumValue());

        if (JediModeEnum.DEFAULT.getEnumValue() == jediMode || JediModeEnum.CONFIG_ONLY.getEnumValue() == jediMode) {
            initializeConfig(applicationContext.getEnvironment());
        }
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
     * Initialize Jedi Configurations Just once
     *
     * @param environment
     */
    private void initializeConfig(ConfigurableEnvironment environment) {
        if (environment.getPropertySources().contains(Constants.JEDI_BOOTSTRAP_PROPERTY_SOURCE_NAME)) {
            //already initialized
            return;
        }

        List<String> executorNames;
        String executors = environment.getProperty(Constants.JEDI_CONFIG_EXECUTORS_KEY);
        if (StringUtils.isBlank(executors)) {
            executorNames = Lists.newArrayList(Constants.JEDI_DEFAULT_EXECUTOR_NAME);
        } else {
            log.debug("Jedi bootstrap executors: {}", executors);
            executorNames = EXECUTOR_SPLITTER.splitToList(executors);
        }

        CompositePropertySource composite =
                new CompositePropertySource(Constants.JEDI_BOOTSTRAP_PROPERTY_SOURCE_NAME);

        for (String executor : executorNames) {
            Config config = ConfigService.getConfig(executor);

            composite.addPropertySource(ConfigPropertySourceFactory.getConfigPropertySource(executor, config));
        }

        environment.getPropertySources().addFirst(composite);
    }
}
