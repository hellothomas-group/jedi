package xyz.hellothomas.jedi.collector.domain;

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
public class MonitorMessage {
    /**
     * id
     */
    private String id;

    /**
     * appId
     */
    private String appId;

    /**
     * namespace名称
     */
    private String namespace;

    /**
     * 消息名称
     */
    private String messageType;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 主机
     */
    private String host;

    /**
     * 记录时间
     */
    private LocalDateTime recordTime;

    /**
     * 生成时间
     */
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;

}