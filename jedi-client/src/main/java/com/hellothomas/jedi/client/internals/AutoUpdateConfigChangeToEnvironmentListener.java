package com.hellothomas.jedi.client.internals;

import com.hellothomas.jedi.client.ConfigChangeListener;
import com.hellothomas.jedi.client.constants.Constants;
import com.hellothomas.jedi.client.model.ConfigChange;
import com.hellothomas.jedi.client.model.ConfigChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.util.CollectionUtils;

import java.util.Set;

public class AutoUpdateConfigChangeToEnvironmentListener implements ConfigChangeListener {
    private static final Logger logger = LoggerFactory.getLogger(AutoUpdateConfigChangeToEnvironmentListener.class);

    private final ApplicationContext applicationContext;

    public AutoUpdateConfigChangeToEnvironmentListener(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onChange(ConfigChangeEvent changeEvent) {
        Set<String> keys = changeEvent.changedKeys();
        if (CollectionUtils.isEmpty(keys)) {
            return;
        }
        ConfigurableEnvironment environment = (ConfigurableEnvironment) applicationContext.getEnvironment();
        PropertySource<?> bootstrapPropertySource =
                environment.getPropertySources().get(Constants.JEDI_BOOTSTRAP_PROPERTY_SOURCE_NAME);

        // not exists
        if (bootstrapPropertySource == null) {
            return;
        }

        for (String key : keys) {
            ConfigChange configChange = changeEvent.getChange(key);
            logger.debug("key: {}, ConfigChange: {}", key, configChange);
        }
    }
}
