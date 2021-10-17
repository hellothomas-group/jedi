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
public class Instance {
    /**
     * 自增Id
     */
    private Long id;

    /**
     * namespaceName
     */
    private String namespaceName;

    /**
     * appId
     */
    private String appId;

    /**
     * instance ip
     */
    private String ip;

    /**
     * 创建时间
     */
    private LocalDateTime dataChangeCreatedTime;

    /**
     * 最后修改时间
     */
    private LocalDateTime dataChangeLastModifiedTime;
}