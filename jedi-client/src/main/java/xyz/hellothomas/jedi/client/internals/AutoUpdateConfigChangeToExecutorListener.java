package xyz.hellothomas.jedi.client.internals;

import com.google.common.base.CaseFormat;
import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;
import xyz.hellothomas.jedi.client.ConfigChangeListener;
import xyz.hellothomas.jedi.client.enums.PropertyChangeType;
import xyz.hellothomas.jedi.client.model.ConfigChange;
import xyz.hellothomas.jedi.client.model.ConfigChangeEvent;
import xyz.hellothomas.jedi.core.internals.executor.JediThreadPoolExecutor;

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

        JediThreadPoolExecutor executor = applicationContext.getBean(changeEvent.getExecutor(),
                JediThreadPoolExecutor.class);
        for (String key : keys) {
            ConfigChange configChange = changeEvent.getChange(key);
            logger.debug("key: {}, ConfigChange: {}", key, configChange);

            if (PropertyChangeType.DELETED == configChange.getChangeType()) {
                continue;
            }

            String jediThreadPoolPropertyName = CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL,
                    PROPERTY_SPLITTER.splitToList(key).get(2));
            switch (jediThreadPoolPropertyName) {
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
                default:
                    logger.info("not supported jediThreadPoolPropertyName:{}", jediThreadPoolPropertyName);
            }
        }
    }
}
