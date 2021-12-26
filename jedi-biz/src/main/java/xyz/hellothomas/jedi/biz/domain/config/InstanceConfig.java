package xyz.hellothomas.jedi.biz.domain.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author
 */
@Getter
@Setter
@ToString
public class InstanceConfig {
    /**
     * 自增Id
     */
    private Long id;

    /**
     * instanceId
     */
    private Long instanceId;

    /**
     * configNamespaceName
     */
    private String configNamespaceName;

    /**
     * configAppId
     */
    private String configAppId;

    /**
     * configExecutorName
     */
    private String configExecutorName;

    /**
     * 发布的Key
     */
    private String releaseKey;

    /**
     * 配置获取时间
     */
    private LocalDateTime releaseDeliveryTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    private LocalDateTime updateTime;
}