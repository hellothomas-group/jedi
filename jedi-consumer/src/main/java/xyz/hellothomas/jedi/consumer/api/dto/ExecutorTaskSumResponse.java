package xyz.hellothomas.jedi.consumer.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Thomas
 */
@Getter
@Setter
@ToString
public class ExecutorTaskSumResponse {
    /**
     * 任务名称
     */
    private String taskName;

    private Long total;

    private Long failure;

    private Long failureRatio;

    private Long executionTimeMax;

    private Long executionTimeMin;

    /**
     * 执行时间95线
     */
    private Long executionTimeLine95;

    /**
     * 执行时间99线
     */
    private Long executionTimeLine99;
}
