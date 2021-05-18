package xyz.hellothomas.jedi.client.internals;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.hellothomas.jedi.client.Config;
import xyz.hellothomas.jedi.client.ConfigChangeListener;
import xyz.hellothomas.jedi.client.constants.Constants;
import xyz.hellothomas.jedi.client.enums.PropertyChangeType;
import xyz.hellothomas.jedi.client.model.ConfigChange;
import xyz.hellothomas.jedi.client.model.ConfigChangeEvent;
import xyz.hellothomas.jedi.client.util.Functions;
import xyz.hellothomas.jedi.core.utils.JediThreadFactory;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 *    Copyright ctripcorp
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * @author Jason Song(song_s@ctrip.com)
 */
public abstract class AbstractConfig implements Config {
    private static final Logger logger = LoggerFactory.getLogger(AbstractConfig.class);

    private static final ExecutorService m_executorService;

    private final List<ConfigChangeListener> m_listeners = Lists.newCopyOnWriteArrayList();
    private final Map<ConfigChangeListener, Set<String>> m_interestedKeys = Maps.newConcurrentMap();
    private final Map<ConfigChangeListener, Set<String>> m_interestedKeyPrefixes = Maps.newConcurrentMap();
    private volatile Cache<String, Integer> m_integerCache;
    private volatile Cache<String, Long> m_longCache;
    private volatile Cache<String, Short> m_shortCache;
    private final List<Cache> allCaches;
    private final AtomicLong m_configVersion; //indicate config version

    static {
        m_executorService = Executors.newCachedThreadPool(JediThreadFactory
                .create("Config", true));
    }

    public AbstractConfig() {
        m_configVersion = new AtomicLong();
        allCaches = Lists.newArrayList();
    }

    @Override
    public void addChangeListener(ConfigChangeListener listener) {
        addChangeListener(listener, null);
    }

    @Override
    public void addChangeListener(ConfigChangeListener listener, Set<String> interestedKeys) {
        addChangeListener(listener, interestedKeys, null);
    }

    @Override
    public void addChangeListener(ConfigChangeListener listener, Set<String> interestedKeys,
                                  Set<String> interestedKeyPrefixes) {
        if (!m_listeners.contains(listener)) {
            m_listeners.add(listener);
            if (interestedKeys != null && !interestedKeys.isEmpty()) {
                m_interestedKeys.put(listener, Sets.newHashSet(interestedKeys));
            }
            if (interestedKeyPrefixes != null && !interestedKeyPrefixes.isEmpty()) {
                m_interestedKeyPrefixes.put(listener, Sets.newHashSet(interestedKeyPrefixes));
            }
        }
    }

    @Override
    public boolean removeChangeListener(ConfigChangeListener listener) {
        m_interestedKeys.remove(listener);
        m_interestedKeyPrefixes.remove(listener);
        return m_listeners.remove(listener);
    }

    @Override
    public Integer getIntProperty(String key, Integer defaultValue) {
        try {
            if (m_integerCache == null) {
                synchronized (this) {
                    if (m_integerCache == null) {
                        m_integerCache = newCache();
                    }
                }
            }

            return getValueFromCache(key, Functions.TO_INT_FUNCTION, m_integerCache, defaultValue);
        } catch (Throwable ex) {
            logger.error("getIntProperty for {} failed, return default value {}, error {}", key,
                    defaultValue, ex);
        }
        return defaultValue;
    }

    @Override
    public Long getLongProperty(String key, Long defaultValue) {
        try {
            if (m_longCache == null) {
                synchronized (this) {
                    if (m_longCache == null) {
                        m_longCache = newCache();
                    }
                }
            }

            return getValueFromCache(key, Functions.TO_LONG_FUNCTION, m_longCache, defaultValue);
        } catch (Throwable ex) {
            logger.error("getLongProperty for {} failed, return default value {}, error {}", key,
                    defaultValue, ex);
        }
        return defaultValue;
    }

    @Override
    public Short getShortProperty(String key, Short defaultValue) {
        try {
            if (m_shortCache == null) {
                synchronized (this) {
                    if (m_shortCache == null) {
                        m_shortCache = newCache();
                    }
                }
            }

            return getValueFromCache(key, Functions.TO_SHORT_FUNCTION, m_shortCache, defaultValue);
        } catch (Throwable ex) {
            logger.error("getShortProperty for {} failed, return default value {}, error {}", key,
                    defaultValue, ex);
        }
        return defaultValue;
    }

    @Override
    public <T> T getProperty(String key, Function<String, T> function, T defaultValue) {
        try {
            String value = getProperty(key, null);

            if (value != null) {
                return function.apply(value);
            }
        } catch (Throwable ex) {
            logger.error("getProperty for {} failed, return default value {}, error {}", key,
                    defaultValue, ex);
        }

        return defaultValue;
    }

    private <T> T getValueFromCache(String key, Function<String, T> parser, Cache<String, T> cache, T defaultValue) {
        T result = cache.getIfPresent(key);

        if (result != null) {
            return result;
        }

        return getValueAndStoreToCache(key, parser, cache, defaultValue);
    }

    private <T> T getValueAndStoreToCache(String key, Function<String, T> parser, Cache<String, T> cache,
                                          T defaultValue) {
        long currentConfigVersion = m_configVersion.get();
        String value = getProperty(key, null);

        if (value != null) {
            T result = parser.apply(value);

            if (result != null) {
                synchronized (this) {
                    if (m_configVersion.get() == currentConfigVersion) {
                        cache.put(key, result);
                    }
                }
                return result;
            }
        }

        return defaultValue;
    }

    private <T> Cache<String, T> newCache() {
        Cache<String, T> cache = CacheBuilder.newBuilder()
                .maximumSize(Constants.maxConfigCacheSize)
                .expireAfterAccess(Constants.configCacheExpireTime, Constants.configCacheExpireTimeUnit)
                .build();
        allCaches.add(cache);
        return cache;
    }

    /**
     * Clear config cache
     */
    protected void clearConfigCache() {
        synchronized (this) {
            for (Cache c : allCaches) {
                if (c != null) {
                    c.invalidateAll();
                }
            }
            m_configVersion.incrementAndGet();
        }
    }

    protected void fireConfigChange(final ConfigChangeEvent changeEvent) {
        for (final ConfigChangeListener listener : m_listeners) {
            // check whether the listener is interested in this change event
            if (!isConfigChangeListenerInterested(listener, changeEvent)) {
                continue;
            }
            m_executorService.submit(new Runnable() {
                @Override
                public void run() {
                    String listenerName = listener.getClass().getName();
                    try {
                        listener.onChange(changeEvent);
                    } catch (Throwable ex) {
                        logger.error("Failed to invoke config change listener {}", listenerName, ex);
                    }
                }
            });
        }
    }

    private boolean isConfigChangeListenerInterested(ConfigChangeListener configChangeListener,
                                                     ConfigChangeEvent configChangeEvent) {
        Set<String> interestedKeys = m_interestedKeys.get(configChangeListener);
        Set<String> interestedKeyPrefixes = m_interestedKeyPrefixes.get(configChangeListener);

        if ((interestedKeys == null || interestedKeys.isEmpty())
                && (interestedKeyPrefixes == null || interestedKeyPrefixes.isEmpty())) {
            return true; // no interested keys means interested in all keys
        }

        if (interestedKeys != null) {
            for (String interestedKey : interestedKeys) {
                if (configChangeEvent.isChanged(interestedKey)) {
                    return true;
                }
            }
        }

        if (interestedKeyPrefixes != null) {
            for (String prefix : interestedKeyPrefixes) {
                for (final String changedKey : configChangeEvent.changedKeys()) {
                    if (changedKey.startsWith(prefix)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    List<ConfigChange> calcPropertyChanges(String executor, Properties previous,
                                           Properties current) {
        if (previous == null) {
            previous = new Properties();
        }

        if (current == null) {
            current = new Properties();
        }

        Set<String> previousKeys = previous.stringPropertyNames();
        Set<String> currentKeys = current.stringPropertyNames();

        Set<String> commonKeys = Sets.intersection(previousKeys, currentKeys);
        Set<String> newKeys = Sets.difference(currentKeys, commonKeys);
        Set<String> removedKeys = Sets.difference(previousKeys, commonKeys);

        List<ConfigChange> changes = Lists.newArrayList();

        for (String newKey : newKeys) {
            changes.add(new ConfigChange(executor, newKey, null, current.getProperty(newKey),
                    PropertyChangeType.ADDED));
        }

        for (String removedKey : removedKeys) {
            changes.add(new ConfigChange(executor, removedKey, previous.getProperty(removedKey), null,
                    PropertyChangeType.DELETED));
        }

        for (String commonKey : commonKeys) {
            String previousValue = previous.getProperty(commonKey);
            String currentValue = current.getProperty(commonKey);
            if (Objects.equal(previousValue, currentValue)) {
                continue;
            }
            changes.add(new ConfigChange(executor, commonKey, previousValue, currentValue,
                    PropertyChangeType.MODIFIED));
        }

        return changes;
    }
}
