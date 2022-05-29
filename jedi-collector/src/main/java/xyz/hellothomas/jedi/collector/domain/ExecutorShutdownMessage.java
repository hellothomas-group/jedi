package xyz.hellothomas.jedi.collector.domain;

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
public class ExecutorShutdownMessage {
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
     * 已执行任务数
     */
    private Long completedTaskCount;
    /**
     * 正在执行任务数
     */
    private Long executingTaskCount;
    /**
     * 未执行任务数
     */
    private Long toExecuteTaskCount;
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