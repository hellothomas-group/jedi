package xyz.hellothomas.jedi.core.dto.consumer;

import javax.validation.constraints.NotBlank;

/**
 * @author Thomas
 * @date 2021/1/3 17:44
 * @description
 * @version 1.0
 */
public class ExecutorTaskNotification extends AbstractNotification {
    public static final String DEFAULT_TASK_NAME = "DEFAULT";

    /**
     * 任务名称
     */
    @NotBlank
    private String taskName = DEFAULT_TASK_NAME;

    /**
     * 线程池名称
     */
    @NotBlank
    private String poolName;

    /**
     * 执行时间
     */
    private long executionTime;

    /**
     * 执行结果
     */
    private boolean isSuccess;

    /**
     * 执行失败原因
     */
    private String failureReason;

    /**
     * 任务附加信息
     */
    private String taskExtraData;

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    @Override
    public String toString() {
        return "ExecutorTaskMessage(id=" + this.getId() + ", appId=" + this.getAppId() + ", namespace" +
                "=" + this.getNamespace() + ", messageType=" + this.getMessageType() + ", recordTime=" + this.getRecordTime() + ", host=" + this.getHost() +
                ", taskName=" + this.taskName + ", poolName=" + this.poolName + ", executionTime=" +
                this.executionTime + ", isSuccess=" + this.isSuccess + ", failureReason=" + this.failureReason + ", " +
                "taskExtraData=" + this.taskExtraData + ")";
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public String getTaskExtraData() {
        return taskExtraData;
    }

    public void setTaskExtraData(String taskExtraData) {
        this.taskExtraData = taskExtraData;
    }

    public boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean success) {
        this.isSuccess = success;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }
}
