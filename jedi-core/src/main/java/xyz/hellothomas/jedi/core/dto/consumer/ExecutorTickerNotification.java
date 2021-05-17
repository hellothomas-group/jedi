package xyz.hellothomas.jedi.core.dto.consumer;

import javax.validation.constraints.NotBlank;

/**
 * @author Thomas
 * @date 2021/1/3 17:44
 * @description
 * @version 1.0
 */
public class ExecutorTickerNotification extends AbstractNotification {
    /**
     * 线程池名称
     */
    @NotBlank
    private String poolName;

    /**
     * 核心线程数
     */
    private int corePoolSize;

    /**
     * 最大允许的线程数
     */
    private int maximumPoolSize;

    /**
     * 池中实时线程数
     */
    private int poolSize;

    /**
     * 池中实时运行线程数
     */
    private int activeCount;

    /**
     * 池中历史最大线程数
     */
    private int largestPoolSize;

    /**
     * 队列类型
     */
    private String queueType;

    /**
     * 队列已使用容量
     */
    private int queueSize;

    /**
     * 队列剩余容量
     */
    private int queueRemainingCapacity;

    /**
     * 任务总数
     */
    private long taskCount;

    /**
     * 已执行任务数
     */
    private long completedTaskCount;

    /**
     * 拒绝任务数
     */
    private long rejectCount;

    /**
     * 线程池是否关闭
     */
    private boolean isShutdown;

    /**
     * 线程池是否终止
     */
    private boolean isTerminated;

    public boolean getIsShutdown() {
        return isShutdown;
    }

    public void setIsShutdown(boolean isShutdown) {
        isShutdown = isShutdown;
    }

    public boolean getIsTerminated() {
        return isTerminated;
    }

    public void setIsTerminated(boolean isTerminated) {
        isTerminated = isTerminated;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public int getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(int activeCount) {
        this.activeCount = activeCount;
    }

    public int getLargestPoolSize() {
        return largestPoolSize;
    }

    public void setLargestPoolSize(int largestPoolSize) {
        this.largestPoolSize = largestPoolSize;
    }

    public String getQueueType() {
        return queueType;
    }

    public void setQueueType(String queueType) {
        this.queueType = queueType;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public int getQueueRemainingCapacity() {
        return queueRemainingCapacity;
    }

    public void setQueueRemainingCapacity(int queueRemainingCapacity) {
        this.queueRemainingCapacity = queueRemainingCapacity;
    }

    public long getRejectCount() {
        return rejectCount;
    }

    public void setRejectCount(long rejectCount) {
        this.rejectCount = rejectCount;
    }

    @Override
    public String toString() {
        return "ExecutorTickerMessage(id=" + this.getId() + ", appId=" + this.getAppId() + ", namespace" +
                "=" + this.getNamespace() + ", messageType=" + this.getMessageType() + ", recordTime" +
                "=" + this.getRecordTime() + ", host=" + this.getHost() + ", poolName=" + this.getPoolName() + ", " +
                "corePoolSize=" + this.getCorePoolSize() +
                ", maximumPoolSize=" + this.getMaximumPoolSize() + ", poolSize=" + this.getPoolSize() + ", " +
                "activeCount=" + this.getActiveCount() + ", largestPoolSize=" + this.getLargestPoolSize() + ", " +
                "queueType=" + this.getQueueType() + ", queueSize=" + this.getQueueSize() + ", queueRemainingCapacity" +
                "=" + this.getQueueRemainingCapacity() + ", taskCount=" + this.getTaskCount() + ", completedTaskCount" +
                "=" + this.getCompletedTaskCount() + ", rejectCount=" + this.getRejectCount() + ", isShutdown=" +
                this.getIsShutdown() + ", isTerminated" + "=" + this.getIsTerminated() + ")";
    }

    public long getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(long taskCount) {
        this.taskCount = taskCount;
    }

    public long getCompletedTaskCount() {
        return completedTaskCount;
    }

    public void setCompletedTaskCount(long completedTaskCount) {
        this.completedTaskCount = completedTaskCount;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }
}
