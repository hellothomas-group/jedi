package com.hellothomas.jedi.client;

import com.hellothomas.jedi.client.internals.ConfigManager;
import com.hellothomas.jedi.client.internals.DefaultConfigManager;

import java.util.List;

public class ConfigService {
    private static final ConfigService s_instance = new ConfigService();

    private volatile ConfigManager m_configManager;

    private ConfigManager getManager() {
        if (m_configManager == null) {
            synchronized (this) {
                if (m_configManager == null) {
                    m_configManager = new DefaultConfigManager();
                }
            }
        }

        return m_configManager;
    }

    /**
     * Get the config instance for the namespace.
     *
     * @param executor the executor of the config
     * @return config instance
     */
    public static Config getConfig(String executor) {
        return s_instance.getManager().getConfig(executor);
    }

    /**
     * Get all config instances
     *
     * @return config instances
     */
    public static List<Config> getConfigs() {
        return s_instance.getManager().getConfigs();
    }
}
