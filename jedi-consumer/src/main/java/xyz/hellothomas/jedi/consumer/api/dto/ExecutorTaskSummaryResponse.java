package xyz.hellothomas.jedi.consumer.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Thomas
 */
@Getter
@Setter
@ToString
public class ExecutorTaskSummaryResponse {
    /**
     * 统计日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate statisticsDate;

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
     * 执行总数
     */
    private Long total;

    /**
     * 执行失败总数
     */
    private Long failure;

    /**
     * 执行失败比例
     */
    private BigDecimal failureRatio;

    /**
     * 执行最长时间
     */
    private Long executionTimeMax;

    /**
     * 执行最短时间
     */
    private Long executionTimeMin;

    /**
     * 执行时间95线
     */
    private Long executionTimeLine95;

    /**
     * 执行时间99线
     */
    private Long executionTimeLine99;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
