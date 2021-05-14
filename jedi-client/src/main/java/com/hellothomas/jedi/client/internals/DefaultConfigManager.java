package com.hellothomas.jedi.client.internals;

import com.google.common.collect.Maps;
import com.hellothomas.jedi.client.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultConfigManager implements ConfigManager {

    private Map<String, Config> m_configs = Maps.newConcurrentMap();
    private RemoteConfigLongPollService remoteConfigLongPollService;

    public DefaultConfigManager() {
        remoteConfigLongPollService = new RemoteConfigLongPollService();
    }

    @Override
    public Config getConfig(String executor) {
        Config config = m_configs.get(executor);

        if (config == null) {
            synchronized (this) {
                config = m_configs.get(executor);

                if (config == null) {
                    ConfigRepository configRepository = new RemoteConfigRepository(executor,
                            remoteConfigLongPollService);
                    config = new DefaultConfig(executor, configRepository);
                    m_configs.put(executor, config);
                }
            }
        }

        return config;
    }

    @Override
    public List<Config> getConfigs() {
        List<Config> configs = new ArrayList<>();
        m_configs.forEach((key, value) -> configs.add(value));
        return configs;
    }
}
