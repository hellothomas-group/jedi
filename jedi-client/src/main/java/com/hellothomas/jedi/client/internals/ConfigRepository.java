package com.hellothomas.jedi.client.internals;

import com.hellothomas.jedi.client.enums.ConfigSourceType;

import java.util.Properties;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public interface ConfigRepository {
    /**
     * Get the config from this repository.
     * @return config
     */
    public Properties getConfig();

    /**
     * Add change listener.
     * @param listener the listener to observe the changes
     */
    public void addChangeListener(RepositoryChangeListener listener);

    /**
     * Remove change listener.
     * @param listener the listener to remove
     */
    public void removeChangeListener(RepositoryChangeListener listener);

    /**
     * Return the config's source type, i.e. where is the config loaded from
     *
     * @return the config's source type
     */
    public ConfigSourceType getSourceType();
}
