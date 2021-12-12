package xyz.hellothomas.jedi.client.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import xyz.hellothomas.jedi.core.internals.executor.JediThreadPoolProperty;

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
public class JediConfig {
    private boolean enable;

    private int mode;

    private String url;

    private String namespace;

    private String appId;

    private int order;

    @NestedConfigurationProperty
    private List<JediThreadPoolProperty> executors = new ArrayList<>();
}
