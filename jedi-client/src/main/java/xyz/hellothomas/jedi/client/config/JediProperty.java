package xyz.hellothomas.jedi.client.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
public class JediProperty {
    private boolean enable;

    /**
     * 0：监控 + 服务端配置
     * 1：仅监控
     * 2：仅服务端配置
     * 3：脱机模式(仅本地配置)
     */
    private int mode;

    private String url;

    private String namespace;

    private String appId;

    private String executors;

    private int order = JEDI_ASYNC_DEFAULT_ORDER;
}
