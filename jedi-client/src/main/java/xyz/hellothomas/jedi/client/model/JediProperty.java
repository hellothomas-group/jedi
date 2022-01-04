package xyz.hellothomas.jedi.client.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import static xyz.hellothomas.jedi.client.constants.Constants.JEDI_ASYNC_DEFAULT_ORDER;

/**
 * @author Thomas
 * @date 2021/4/11 10:12
 * @description
 * @version 1.0
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "jedi")
public class JediProperty {
    /**
     * 开启 Jedi
     */
    private boolean enable;

    /**
     * 客户端模式
     * 0：监控 + 服务端配置
     * 1：仅监控
     * 2：仅服务端配置
     * 3：脱机模式(仅本地配置)
     */
    private int mode;

    /**
     * config-service url
     */
    private String url;

    /**
     * 环境
     */
    private String namespace;

    /**
     * 应用名
     */
    private String appId;

    /**
     * 线程池名称集合
     * <p>
     * Use the dashed notation to specify each executor, that is all lower case with a "-"
     * to separate words (e.g. {@code jedi-default-executor}).
     */
    private String executors;

    /**
     * {@link xyz.hellothomas.jedi.client.aop.JediAsyncAspect} order
     */
    private int order = JEDI_ASYNC_DEFAULT_ORDER;

    /**
     * 持久化配置
     */
    @NestedConfigurationProperty
    private JediPersistenceProperty persistence;
}
