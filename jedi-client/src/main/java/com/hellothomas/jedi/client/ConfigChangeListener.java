package com.hellothomas.jedi.client;

import com.hellothomas.jedi.client.model.ConfigChangeEvent;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public interface ConfigChangeListener {
    /**
     * Invoked when there is any config change for the namespace.
     * @param changeEvent the event for this change
     */
    public void onChange(ConfigChangeEvent changeEvent);
}
