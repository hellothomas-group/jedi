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
     * 等待时间
     */
    private Long waitTime;

    /**
     * 执行时间
     */
    private Long executionTime;

    /**
     * 0:registered, 1:doing, 2:success, 3:fail, 4:rejected
     */
    private Integer status;

    /**
     * exitCode
     */
    private String exitCode;

    /**
     * exitMessage
     */
    private String exitMessage;

    /**
     * 任务附加信息
     */
    private String taskExtraData;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 1: recoverable, 0: no recoverable
     */
    private Boolean isRecoverable;

    /**
     * 主机
     */
    private String host;

    /**
     * traceId
     */
    private String traceId;

    /**
     * previousId
     */
    private String previousId;

    /**
     * dataSourceName
     */
    private String dataSourceName;

    /**
     * 记录时间
     */
    private LocalDateTime recordTime;

    /**
     * 1: retried, 0: no retried
     */
    private Boolean isRetried;

    /**
     * retriedId
     */
    private String retriedId;

    /**
     * 最后修改人
     */
    private String updateUser;

    /**
     * 生成时间
     */
    private LocalDateTime createTime;
    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 版本号
     */
    private Integer version;
}
