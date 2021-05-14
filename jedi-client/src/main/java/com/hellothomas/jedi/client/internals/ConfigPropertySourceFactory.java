package com.hellothomas.jedi.client.internals;

import com.google.common.collect.Lists;
import com.hellothomas.jedi.client.Config;

import java.util.List;

public class ConfigPropertySourceFactory {

    private static final List<ConfigPropertySource> configPropertySources = Lists.newLinkedList();

    public static ConfigPropertySource getConfigPropertySource(String name, Config source) {
        ConfigPropertySource configPropertySource = new ConfigPropertySource(name, source);

        configPropertySources.add(configPropertySource);

        return configPropertySource;
    }

    public static List<ConfigPropertySource> getAllConfigPropertySources() {
        return Lists.newLinkedList(configPropertySources);
    }
}
