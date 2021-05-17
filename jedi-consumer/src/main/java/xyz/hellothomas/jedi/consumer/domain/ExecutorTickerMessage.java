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
public class ExecutorTickerMessage {
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
     * 核心线程数
     */
    private Integer corePoolSize;
    /**
     * 最大允许的线程数
     */
    private Integer maximumPoolSize;
    /**
     * 实时线程数
     */
    private Integer poolSize;
    /**
     * 实时运行线程数
     */
    private Integer activeCount;
    /**
     * 历史最大线程数
     */
    private Integer largestPoolSize;
    /**
     * 队列类型
     */
    private String queueType;
    /**
     * 队列已使用容量
     */
    private Integer queueSize;
    /**
     * 队列剩余容量
     */
    private Integer queueRemainingCapacity;
    /**
     * 任务总数
     */
    private Long taskCount;
    /**
     * 已执行任务数
     */
    private Long completedTaskCount;
    /**
     * 拒绝任务数
     */
    private Long rejectCount;
    /**
     * 线程池是否关闭
     */
    private Boolean isShutdown;
    /**
     * 线程池是否终止
     */
    private Boolean isTerminated;
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