package com.hellothomas.jedi.client.internals;

import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.RateLimiter;
import com.hellothomas.jedi.client.enums.ConfigSourceType;
import com.hellothomas.jedi.client.enums.PropertyChangeType;
import com.hellothomas.jedi.client.model.ConfigChange;
import com.hellothomas.jedi.client.model.ConfigChangeEvent;
import com.hellothomas.jedi.client.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class DefaultConfig extends AbstractConfig implements RepositoryChangeListener {
    private static final Logger logger = LoggerFactory.getLogger(DefaultConfig.class);
    private final String m_executor;
    private final AtomicReference<Properties> m_configProperties;
    private final ConfigRepository m_configRepository;
    private final RateLimiter m_warnLogRateLimiter;

    private volatile ConfigSourceType m_sourceType = ConfigSourceType.NONE;

    /**
     * Constructor.
     *
     * @param executor        the executor of this config instance
     * @param configRepository the config repository for this config instance
     */
    public DefaultConfig(String executor, ConfigRepository configRepository) {
        m_executor = executor;
        m_configRepository = configRepository;
        m_configProperties = new AtomicReference<>(new Properties());
        m_warnLogRateLimiter = RateLimiter.create(0.017); // 1 warning log output per minute
        initialize();
    }

    private void initialize() {
        try {
            updateConfig(m_configRepository.getConfig(), m_configRepository.getSourceType());
        } catch (Throwable ex) {
            logger.warn("Init Jedi Local Config failed - namespace: {}, reason: {}.",
                    m_executor, ExceptionUtil.getDetailMessage(ex));
        } finally {
            //register the change listener no matter config repository is working or not
            //so that whenever config repository is recovered, config could get changed
            m_configRepository.addChangeListener(this);
        }
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        // step 1: check system properties, i.e. -Dkey=value
        String value = System.getProperty(key);

        // step 2: check local cached properties file
        if (value == null && m_configProperties.get() != null) {
            value = m_configProperties.get().getProperty(key);
        }

        /**
         * step 3: check env variable, i.e. PATH=...
         * normally system environment variables are in UPPERCASE, however there might be exceptions.
         * so the caller should provide the key in the right case
         */
        if (value == null) {
            value = System.getenv(key);
        }

        if (value == null && m_configProperties.get() == null && m_warnLogRateLimiter.tryAcquire()) {
            logger.warn("Could not load config for executor {} from Jedi, please check whether the configs are " +
                    "released in Jedi! Return default value now!", m_executor);
        }

        return value == null ? defaultValue : value;
    }

    @Override
    public Set<String> getPropertyNames() {
        Properties properties = m_configProperties.get();
        if (properties == null) {
            return Collections.emptySet();
        }

        return stringPropertyNames(properties);
    }

    @Override
    public ConfigSourceType getSourceType() {
        return m_sourceType;
    }

    private Set<String> stringPropertyNames(Properties properties) {
        //jdk9以下版本Properties#enumerateStringProperties方法存在性能问题，keys() + get(k) 重复迭代, jdk9之后改为entrySet遍历.
        Map<String, String> h = new LinkedHashMap<>();
        for (Map.Entry<Object, Object> e : properties.entrySet()) {
            Object k = e.getKey();
            Object v = e.getValue();
            if (k instanceof String && v instanceof String) {
                h.put((String) k, (String) v);
            }
        }
        return h.keySet();
    }

    @Override
    public synchronized void onRepositoryChange(String executor, Properties newProperties) {
        if (newProperties.equals(m_configProperties.get())) {
            return;
        }

        ConfigSourceType sourceType = m_configRepository.getSourceType();
        Properties newConfigProperties = new Properties();
        newConfigProperties.putAll(newProperties);

        Map<String, ConfigChange> actualChanges = updateAndCalcConfigChanges(newConfigProperties, sourceType);

        //check double checked result
        if (actualChanges.isEmpty()) {
            return;
        }

        this.fireConfigChange(new ConfigChangeEvent(executor, actualChanges));

        logger.debug("Jedi.Client.ConfigChanges {}", executor);
    }

    private void updateConfig(Properties newConfigProperties, ConfigSourceType sourceType) {
        m_configProperties.set(newConfigProperties);
        m_sourceType = sourceType;
    }

    private Map<String, ConfigChange> updateAndCalcConfigChanges(Properties newConfigProperties,
                                                                 ConfigSourceType sourceType) {
        List<ConfigChange> configChanges =
                calcPropertyChanges(m_executor, m_configProperties.get(), newConfigProperties);

        ImmutableMap.Builder<String, ConfigChange> actualChanges =
                new ImmutableMap.Builder<>();

        /** === Double check since DefaultConfig has multiple config sources ==== **/

        //1. use getProperty to update configChanges's old value
        for (ConfigChange change : configChanges) {
            change.setOldValue(this.getProperty(change.getPropertyName(), change.getOldValue()));
        }

        //2. update m_configProperties
        updateConfig(newConfigProperties, sourceType);
        clearConfigCache();

        //3. use getProperty to update configChange's new value and calc the final changes
        for (ConfigChange change : configChanges) {
            change.setNewValue(this.getProperty(change.getPropertyName(), change.getNewValue()));
            switch (change.getChangeType()) {
                case ADDED:
                    if (Objects.equals(change.getOldValue(), change.getNewValue())) {
                        break;
                    }
                    if (change.getOldValue() != null) {
                        change.setChangeType(PropertyChangeType.MODIFIED);
                    }
                    actualChanges.put(change.getPropertyName(), change);
                    break;
                case MODIFIED:
                    if (!Objects.equals(change.getOldValue(), change.getNewValue())) {
                        actualChanges.put(change.getPropertyName(), change);
                    }
                    break;
                case DELETED:
                    if (Objects.equals(change.getOldValue(), change.getNewValue())) {
                        break;
                    }
                    if (change.getNewValue() != null) {
                        change.setChangeType(PropertyChangeType.MODIFIED);
                    }
                    actualChanges.put(change.getPropertyName(), change);
                    break;
                default:
                    //do nothing
                    break;
            }
        }
        return actualChanges.build();
    }
}
