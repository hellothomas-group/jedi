package xyz.hellothomas.jedi.collector.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author Thomas
 */
@Getter
@Setter
@ToString
public class ExecutorTaskDetailResponse {
    /**
     * id
     */
    private String id;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 1: recoverable, 0: no recoverable
     */
    private Boolean isRecoverable;

    /**
     * 1: recovered, 0: no recovered
     */
    private Boolean isRecovered;

    /**
     * 主机
     */
    private String host;

    /**
     * traceId
     */
    private String traceId;

    /**
     * 1: byRetryer, 0: no byRetryer
     */
    private Boolean isByRetryer;

    /**
     * previousId
     */
    private String previousId;

    /**
     * parentId
     */
    private String parentId;

    /**
     * 1: true, 0: false
     */
    private Boolean isExecutedByParentTaskThread;

    /**
     * dataSourceName
     */
    private String dataSourceName;

    /**
     * 1: byPersistent, 0: no byPersistent
     */
    private Boolean isPersistent;

    /**
     * 记录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recordTime;

    /**
     * 1: retried, 0: no retried
     */
    private Boolean isRetried;

    /**
     * retryId
     */
    private String retryId;

    /**
     * 最后修改人
     */
    private String updateUser;
}
