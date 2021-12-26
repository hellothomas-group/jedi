package xyz.hellothomas.jedi.consumer.domain.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Thomas
 * @date 2021/7/13 17:21
 * @descripton
 * @version 1.0
 */
@Setter
@Getter
@ToString
public class ExecutorTaskSummary {

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
     * 执行总数
     */
    private Long total = 0L;

    /**
     * 执行失败总数
     */
    private Long failure = 0L;

    /**
     * 执行失败比例
     */
    private BigDecimal failureRatio = BigDecimal.ZERO;

    /**
     * 执行最长时间
     */
    private Long executionTimeMax = 0L;

    /**
     * 执行最短时间
     */
    private Long executionTimeMin = 0L;

    /**
     * 执行时间95线
     */
    private Long executionTimeLine95 = 0L;

    /**
     * 执行时间99线
     */
    private Long executionTimeLine99 = 0L;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    private LocalDateTime updateTime;
}
