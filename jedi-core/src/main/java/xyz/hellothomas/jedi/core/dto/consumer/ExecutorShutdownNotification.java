package xyz.hellothomas.jedi.core.dto.consumer;

import javax.validation.constraints.NotBlank;


/**
 * @author Thomas
 * @date 2021/1/23 15:49
 * @description
 * @version 1.0
 */
public class ExecutorShutdownNotification extends AbstractNotification {
    /**
     * 线程池名称
     */
    @NotBlank
    private String poolName;

    /**
     * 已执行任务数
     */
    private long completedTaskCount;

    /**
     * 正在执行任务数
     */
    private long executingTaskCount;

    /**
     * 未执行任务数
     */
    private long toExecuteTaskCount;

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public long getCompletedTaskCount() {
        return completedTaskCount;
    }

    public void setCompletedTaskCount(long completedTaskCount) {
        this.completedTaskCount = completedTaskCount;
    }

    public long getExecutingTaskCount() {
        return executingTaskCount;
    }

    public void setExecutingTaskCount(long executingTaskCount) {
        this.executingTaskCount = executingTaskCount;
    }

    public long getToExecuteTaskCount() {
        return toExecuteTaskCount;
    }

    public void setToExecuteTaskCount(long toExecuteTaskCount) {
        this.toExecuteTaskCount = toExecuteTaskCount;
    }

    @Override
    public String toString() {
        return "ExecutorShutdownMessage(id=" + this.getId() + ", appId=" + this.getAppId() + ", namespace" +
                "=" + this.getNamespace() + ", messageType=" + this.getMessageType() + ", " +
                "recordTime=" + this.getRecordTime() + ", host=" + this.getHost() +
                ", poolName=" + this.poolName + ", completedTaskCount=" + this.completedTaskCount + ", " +
                "executingTaskCount=" +
                this.executingTaskCount + ", toExecuteTaskCount=" + this.toExecuteTaskCount + ")";
    }
}
