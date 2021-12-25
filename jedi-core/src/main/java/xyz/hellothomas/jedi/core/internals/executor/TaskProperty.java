package xyz.hellothomas.jedi.core.internals.executor;

import java.time.LocalDateTime;

public class TaskProperty {
    private String id;
    private String namespaceName;
    private String appId;
    private String executorName;
    private String taskName;
    private String taskExtraData;
    private LocalDateTime createTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String exitCode;
    private String exitMessage;
    private String beanName;
    private String beanTypeName;
    private String methodName;
    private String methodParamTypes;
    private String methodArguments;
    private boolean recoverable;
    private String host;
    private String traceId;
    private String previousId;
    private boolean persistent;
    private String dataSourceName;

    public String getId() {
        return this.id;
    }

    public String getNamespaceName() {
        return this.namespaceName;
    }

    public String getAppId() {
        return this.appId;
    }

    public String getExecutorName() {
        return this.executorName;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public String getTaskExtraData() {
        return this.taskExtraData;
    }

    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    public String getStatus() {
        return this.status;
    }

    public String getExitCode() {
        return this.exitCode;
    }

    public String getExitMessage() {
        return this.exitMessage;
    }

    public String getBeanName() {
        return this.beanName;
    }

    public String getBeanTypeName() {
        return this.beanTypeName;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public String getMethodParamTypes() {
        return this.methodParamTypes;
    }

    public String getMethodArguments() {
        return this.methodArguments;
    }

    public boolean isRecoverable() {
        return recoverable;
    }

    public String getHost() {
        return host;
    }

    public String getTraceId() {
        return this.traceId;
    }

    public String getPreviousId() {
        return this.previousId;
    }

    public boolean isPersistent() {
        return this.persistent;
    }

    public String getDataSourceName() {
        return this.dataSourceName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNamespaceName(String namespaceName) {
        this.namespaceName = namespaceName;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskExtraData(String taskExtraData) {
        this.taskExtraData = taskExtraData;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setExitCode(String exitCode) {
        this.exitCode = exitCode;
    }

    public void setExitMessage(String exitMessage) {
        this.exitMessage = exitMessage;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public void setBeanTypeName(String beanTypeName) {
        this.beanTypeName = beanTypeName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setMethodParamTypes(String methodParamTypes) {
        this.methodParamTypes = methodParamTypes;
    }

    public void setMethodArguments(String methodArguments) {
        this.methodArguments = methodArguments;
    }

    public void setRecoverable(boolean recoverable) {
        this.recoverable = recoverable;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public void setPreviousId(String previousId) {
        this.previousId = previousId;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    @Override
    public String toString() {
        return "TaskProperty(id=" + this.getId() + ", namespaceName=" + this.getNamespaceName() + ", appId=" + this.getAppId() + ", executorName=" + this.getExecutorName() + ", taskName=" + this.getTaskName() + ", taskExtraData=" + this.getTaskExtraData() + ", createTime=" + this.getCreateTime() + ", startTime=" + this.getStartTime() + ", endTime=" + this.getEndTime() + ", status=" + this.getStatus() + ", exitCode=" + this.getExitCode() + ", exitMessage=" + this.getExitMessage() + ", beanName=" + this.getBeanName() + ", beanTypeName=" + this.getBeanTypeName() + ", methodName=" + this.getMethodName() + ", methodParamTypes=" + this.getMethodParamTypes() + ", methodArguments=" + this.getMethodArguments() + ", recoverable=" + this.isRecoverable() + ", host=" + this.getHost() + ", traceId=" + this.getTraceId() + ", previousId=" + this.getPreviousId() + ", persistent=" + this.isPersistent() + ", dataSourceName=" + this.getDataSourceName() + ")";
    }
}
