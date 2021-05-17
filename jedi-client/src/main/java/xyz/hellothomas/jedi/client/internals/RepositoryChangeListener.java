package xyz.hellothomas.jedi.client.internals;

import java.util.Properties;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public interface RepositoryChangeListener {
    /**
     * Invoked when config repository changes.
     * @param executor the executor of this repository change
     * @param properties the properties after change
     */
    public void onRepositoryChange(String executor, Properties properties);
}
