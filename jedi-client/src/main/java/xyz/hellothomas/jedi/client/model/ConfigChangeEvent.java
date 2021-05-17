package xyz.hellothomas.jedi.client.model;

import java.util.Map;
import java.util.Set;

/**
 * A change event when a executor's config is changed.
 */
public class ConfigChangeEvent {
    private final String m_executor;
    private final Map<String, ConfigChange> m_changes;

    /**
     * Constructor.
     * @param executor the executor of this change
     * @param changes the actual changes
     */
    public ConfigChangeEvent(String executor,
                             Map<String, ConfigChange> changes) {
        m_executor = executor;
        m_changes = changes;
    }

    /**
     * Get the keys changed.
     * @return the list of the keys
     */
    public Set<String> changedKeys() {
        return m_changes.keySet();
    }

    /**
     * Get a specific change instance for the key specified.
     * @param key the changed key
     * @return the change instance
     */
    public ConfigChange getChange(String key) {
        return m_changes.get(key);
    }

    /**
     * Check whether the specified key is changed
     * @param key the key
     * @return true if the key is changed, false otherwise.
     */
    public boolean isChanged(String key) {
        return m_changes.containsKey(key);
    }

    /**
     * Get the executor of this change event.
     * @return the executor
     */
    public String getExecutor() {
        return m_executor;
    }
}
