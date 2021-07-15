package xyz.hellothomas.jedi.consumer.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Thomas
 */
@Getter
@Setter
@ToString
public class ExecutorTaskStatisticsResponse {
    /**
     * namespaceName
     */
    private String namespaceName;

    /**
     * appId
     */
    private String appId;

    /**
     * executorName
     */
    private String executorName;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 执行总数
     */
    private Integer total;

    /**
     * 执行失败总数
     */
    private Integer failure;

    /**
     * 执行失败比例
     */
    private BigDecimal failureratio;

    /**
     * 执行最长时间
     */
    private Integer executionTimeMax;

    /**
     * 执行最短时间
     */
    private Integer executionTimeMin;

    /**
     * 执行时间95线
     */
    private Integer executionTimeLine95;

    /**
     * 执行时间99线
     */
    private Integer executionTimeLine99;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataChangeCreatedTime;

    /**
     * 最后修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataChangeLastModifiedTime;
}
