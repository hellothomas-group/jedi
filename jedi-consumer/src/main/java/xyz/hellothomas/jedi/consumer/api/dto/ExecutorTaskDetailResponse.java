package xyz.hellothomas.jedi.consumer.api.dto;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recordTime;
}
