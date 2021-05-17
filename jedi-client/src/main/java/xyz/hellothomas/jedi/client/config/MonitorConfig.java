package xyz.hellothomas.jedi.client.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import xyz.hellothomas.jedi.core.internals.executor.DynamicThreadPoolProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Thomas
 * @date 2021/4/11 10:12
 * @description
 * @version 1.0
 */
@Getter
@Setter
@ToString
public class MonitorConfig {
    private boolean enable;

    private String url;

    private String namespace;

    private String appId;

    @NestedConfigurationProperty
    private List<DynamicThreadPoolProperty> executors = new ArrayList<>();
}
