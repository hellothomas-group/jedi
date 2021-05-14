package com.hellothomas.jedi.client.internals;

import com.google.common.base.CaseFormat;
import com.google.common.base.Splitter;
import com.hellothomas.jedi.client.ConfigChangeListener;
import com.hellothomas.jedi.client.enums.PropertyChangeType;
import com.hellothomas.jedi.client.model.ConfigChange;
import com.hellothomas.jedi.client.model.ConfigChangeEvent;
import com.hellothomas.jedi.core.internals.executor.DynamicThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class AutoUpdateConfigChangeToExecutorListener implements ConfigChangeListener {
    private static final Logger logger = LoggerFactory.getLogger(AutoUpdateConfigChangeToExecutorListener.class);
    private static final Splitter PROPERTY_SPLITTER = Splitter.on(".").omitEmptyStrings().trimResults();

    private final ApplicationContext applicationContext;

    public AutoUpdateConfigChangeToExecutorListener(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onChange(ConfigChangeEvent changeEvent) {
        Set<String> keys = changeEvent.changedKeys();
        if (CollectionUtils.isEmpty(keys)) {
            return;
        }

        if (!applicationContext.containsBean(changeEvent.getExecutor())) {
            logger.warn("容器中无executor:{}", changeEvent.getExecutor());
            return;
        }

        DynamicThreadPoolExecutor executor = applicationContext.getBean(changeEvent.getExecutor(),
                DynamicThreadPoolExecutor.class);
        for (String key : keys) {
            ConfigChange configChange = changeEvent.getChange(key);
            logger.debug("key: {}, ConfigChange: {}", key, configChange);

            if (PropertyChangeType.DELETED == configChange.getChangeType()) {
                continue;
            }

            String dynamicThreadPoolPropertyName = CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL,
                    PROPERTY_SPLITTER.splitToList(key).get(2));
            switch (dynamicThreadPoolPropertyName) {
                case "corePoolSize":
                    executor.setCorePoolSize(Integer.parseInt(configChange.getNewValue()));
                    break;
                case "maxPoolSize":
                    executor.setMaximumPoolSize(Integer.parseInt(configChange.getNewValue()));
                    break;
                case "keepAliveSeconds":
                    executor.setKeepAliveTime(Long.parseLong(configChange.getNewValue()), TimeUnit.SECONDS);
                    break;
                case "tickerCycle":
                    executor.setTickerCycle(Integer.parseInt(configChange.getNewValue()));
                    break;
                case "allowCoreThreadTimeOut":
                    executor.allowCoreThreadTimeOut(Boolean.parseBoolean(configChange.getNewValue()));
                    break;
            }
        }
    }
}
