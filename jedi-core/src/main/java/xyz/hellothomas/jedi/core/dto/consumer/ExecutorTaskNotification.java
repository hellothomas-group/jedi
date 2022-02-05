package xyz.hellothomas.jedi.core.dto.consumer;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

import static xyz.hellothomas.jedi.core.constants.Constants.JEDI_DEFAULT_TASK_NAME;

/**
 * @author Thomas
 * @date 2021/1/3 17:44
 * @description
 * @version 1.0
 */
public class ExecutorTaskNotification extends AbstractNotification {
    /**
     * 任务名称
     */
    @NotBlank
    private String taskName = JEDI_DEFAULT_TASK_NAME;

    /**
     * 线程池名称
     */
    @NotBlank
    private String poolName;

    /**
     * 等待时间
     */
    private long waitTime;

    /**
     * 执行时间
     */
    private long executionTime;

    /**
     * 执行结果
     */
    private int status;

    /**
     * exitCode
     */
    private String exitCode;

    /**
     * 执行失败原因
     */
    private String exitMessage;

    /**
     * 任务附加信息
     */
    private String taskExtraData;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 1: recoverable, 0: no recoverable
     */
    private boolean isRecoverable;

    /**
     * 1: recovered, 0: no recovered
     */
    private boolean isRecovered;

    /**
     * traceId
     */
    private String traceId;

    /**
     * 1: byRetryer, 0: no byRetryer
     */
    private boolean isByRetryer;

    /**
     * previousId
     */
    private String previousId;

    /**
     * parentId
     */
    private String parentId;

    /**
     * 1: true, 0: false
     */
    private boolean isExecutedByParentTaskThread;

    /**
     * dataSourceName
     */
    private String dataSourceName;

    /**
     * 1: byPersistent, 0: no byPersistent
     */
    private boolean isPersistent;

    /**
     * 最后修改人
     */
    private String updateUser;

    public String getTaskName() {
        return this.taskName;
    }

    public String getPoolName() {
        return this.poolName;
    }

    public long getWaitTime() {
        return this.waitTime;
    }

    public long getExecutionTime() {
        return this.executionTime;
    }

    public int getStatus() {
        return this.status;
    }

    public String getExitCode() {
        return this.exitCode;
    }

    public String getExitMessage() {
        return this.exitMessage;
    }

    public String getTaskExtraData() {
        return this.taskExtraData;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    public boolean getIsRecoverable() {
        return this.isRecoverable;
    }

    public boolean getIsRecovered() {
        return this.isRecovered;
    }

    public String getTraceId() {
        return this.traceId;
    }

    public boolean getIsByRetryer() {
        return this.isByRetryer;
    }

    public String getPreviousId() {
        return this.previousId;
    }

    public String getParentId() {
        return parentId;
    }

    public boolean getIsExecutedByParentTaskThread() {
        return isExecutedByParentTaskThread;
    }

    public String getDataSourceName() {
        return this.dataSourceName;
    }

    public boolean getIsPersistent() {
        return this.isPersistent;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setExitCode(String exitCode) {
        this.exitCode = exitCode;
    }

    public void setExitMessage(String exitMessage) {
        this.exitMessage = exitMessage;
    }

    public void setTaskExtraData(String taskExtraData) {
        this.taskExtraData = taskExtraData;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setIsRecoverable(boolean isRecoverable) {
        this.isRecoverable = isRecoverable;
    }

    public void setIsRecovered(boolean isRecovered) {
        this.isRecovered = isRecovered;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public void setIsByRetryer(boolean isByRetryer) {
        this.isByRetryer = isByRetryer;
    }

    public void setPreviousId(String previousId) {
        this.previousId = previousId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setIsExecutedByParentTaskThread(boolean executedByParentTaskThread) {
        isExecutedByParentTaskThread = executedByParentTaskThread;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public void setIsPersistent(boolean isPersistent) {
        this.isPersistent = isPersistent;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    @Override
    public String toString() {
        return "ExecutorTaskNotification(taskName=" + this.getTaskName() + ", poolName=" + this.getPoolName() + ", " +
                "waitTime=" + this.getWaitTime() + ", executionTime=" + this.getExecutionTime() + ", status=" + this.getStatus() + ", exitCode=" + this.getExitCode() + ", exitMessage=" + this.getExitMessage() + ", taskExtraData=" + this.getTaskExtraData() + ", endTime=" + this.getEndTime() + ", isRecoverable=" + this.getIsRecoverable() + ", isRecovered=" + this.getIsRecovered() + ", traceId=" + this.getTraceId() + ", isByRetryer=" + this.getIsByRetryer() + ", previousId=" + this.getPreviousId() + ", parentId=" + this.getParentId() + ", isExecutedByParentTaskThread=" + this.getIsExecutedByParentTaskThread() + ", dataSourceName=" + this.getDataSourceName() + ", isPersistent=" + this.getIsPersistent() + ", updateUser=" + this.getUpdateUser() + ")";
    }
}
