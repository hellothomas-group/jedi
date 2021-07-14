package xyz.hellothomas.jedi.consumer.domain;

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
public class ExecutorTaskMessage {
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
     * 线程池名称
     */
    private String poolName;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 执行时间
     */
    private Long executionTime;

    /**
     * 1: success, 0: fail
     */
    private Boolean isSuccess;

    /**
     * 失败原因
     */
    private String failureReason;

    /**
     * 任务附加信息
     */
    private String taskExtraData;

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
