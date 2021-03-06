package xyz.hellothomas.jedi.collector.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author
 */
@Getter
@Setter
@ToString
public class ExecutorTaskStatisticsHistory {
    /**
     * Id
     */
    private Long id;

    /**
     * 统计日期
     */
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
     * 任务名称
     */
    private String taskName;

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
     * 最长总时间
     */
    private Long totalTimeMax;

    /**
     * 最短总时间
     */
    private Long totalTimeMin;

    /**
     * 等待最长时间
     */
    private Long waitTimeMax;

    /**
     * 等待最短时间
     */
    private Long waitTimeMin;

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
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 版本号
     */
    private Integer version;
}
