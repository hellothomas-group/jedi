package xyz.hellothomas.jedi.client.internals;

import xyz.hellothomas.jedi.client.Config;

import java.util.List;

public interface ConfigManager {
    /**
     * Get the config instance for the executor specified.
     * @param executor the executor
     * @return the config instance for the executor
     */
    public Config getConfig(String executor);

    /**
     * Get all config instances
     * @return the config instances
     */
    public List<Config> getConfigs();
}
