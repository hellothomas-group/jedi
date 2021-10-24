package xyz.hellothomas.jedi.client.enums;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Thomas
 */

@Getter
@ToString
public enum JediModeEnum {
    /**
     * 0：监控 + 服务端配置
     */
    DEFAULT(0),
    /**
     * 1：仅监控
     */
    MONITOR_ONLY(1),
    /**
     * 2：仅服务端配置
     */
    CONFIG_ONLY(2),
    /**
     * 3：脱机模式(仅本地配置)
     */
    OFFLINE(3),
    ;

    private int enumValue;

    JediModeEnum(int enumValue) {
        this.enumValue = enumValue;
    }
}
