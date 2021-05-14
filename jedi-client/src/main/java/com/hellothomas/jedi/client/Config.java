package com.hellothomas.jedi.client;

import com.google.common.base.Function;
import com.hellothomas.jedi.client.enums.ConfigSourceType;

import java.util.Set;

/**
 * @ClassName Config
 * @Author Thomas
 * @Date 2021/4/26 22:01
 * @Description
 * @Version 1.0
 */
public interface Config {
    /**
     * Return the property value with the given key, or {@code defaultValue} if the key doesn't exist.
     *
     * @param key          the property name
     * @param defaultValue the default value when key is not found or any error occurred
     * @return the property value
     */
    public String getProperty(String key, String defaultValue);

    /**
     * Return the integer property value with the given key, or {@code defaultValue} if the key
     * doesn't exist.
     *
     * @param key          the property name
     * @param defaultValue the default value when key is not found or any error occurred
     * @return the property value as integer
     */
    public Integer getIntProperty(String key, Integer defaultValue);

    /**
     * Return the long property value with the given key, or {@code defaultValue} if the key doesn't
     * exist.
     *
     * @param key          the property name
     * @param defaultValue the default value when key is not found or any error occurred
     * @return the property value as long
     */
    public Long getLongProperty(String key, Long defaultValue);

    /**
     * Return the short property value with the given key, or {@code defaultValue} if the key doesn't
     * exist.
     *
     * @param key          the property name
     * @param defaultValue the default value when key is not found or any error occurred
     * @return the property value as short
     */
    public Short getShortProperty(String key, Short defaultValue);

    /**
     * Add change listener to this config instance, will be notified when any key is changed in this namespace.
     *
     * @param listener the config change listener
     */
    public void addChangeListener(ConfigChangeListener listener);

    /**
     * Add change listener to this config instance, will only be notified when any of the interested keys is changed
     * in this namespace.
     *
     * @param listener the config change listener
     * @param interestedKeys the keys interested by the listener
     *
     * @since 1.0.0
     */
    public void addChangeListener(ConfigChangeListener listener, Set<String> interestedKeys);

    /**
     * Add change listener to this config instance, will only be notified when any of the interested keys is changed
     * in this namespace.
     *
     * @param listener the config change listener
     * @param interestedKeys the keys that the listener is interested in
     * @param interestedKeyPrefixes the key prefixes that the listener is interested in,
     *                              e.g. "spring." means that {@code listener} is interested in keys that starts with
     *                              "spring.", such as "spring.banner", "spring.jpa", etc.
     *                              and "application" means that {@code listener} is interested in keys that starts
     *                              with "application", such as "applicationName", "application.port", etc.
     *
     * @since 1.0.0
     */
    public void addChangeListener(ConfigChangeListener listener, Set<String> interestedKeys,
                                  Set<String> interestedKeyPrefixes);

    /**
     * Remove the change listener
     *
     * @param listener the specific config change listener to remove
     * @return true if the specific config change listener is found and removed
     *
     * @since 1.0.0
     */
    public boolean removeChangeListener(ConfigChangeListener listener);

    /**
     * Return a set of the property names
     *
     * @return the property names
     */
    public Set<String> getPropertyNames();

    /**
     * Return the user-defined property value with the given key, or {@code defaultValue} if the key doesn't exist.
     *
     * @param key          the property name
     * @param function     the transform {@link Function}. from String to user-defined type
     * @param defaultValue the default value when key is not found or any error occurred
     * @param <T>          user-defined type
     * @return the property value
     *
     * @since 1.0.0
     */
    public <T> T getProperty(String key, Function<String, T> function, T defaultValue);

    /**
     * Return the config's source type, i.e. where is the config loaded from
     *
     * @return the config's source type
     *
     * @since 1.0.0
     */
    public ConfigSourceType getSourceType();
}
