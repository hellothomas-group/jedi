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
    private int status;
    private String exitCode;
    private String exitMessage;
    private String beanName;
    private String beanTypeName;
    private String methodName;
    private String methodParamTypes;
    private String methodArguments;
    private boolean recoverable;
    private boolean recovered;
    private String host;
    private String traceId;
    private boolean byRetryer;
    private String previousId;
    private String parentId;
    private String dataSourceName;
    private String lastUpdatedUser;

    private boolean persistent;
    private boolean initialized;

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

    public int getStatus() {
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

    public boolean isRecovered() {
        return recovered;
    }

    public String getHost() {
        return host;
    }

    public String getTraceId() {
        return this.traceId;
    }

    public boolean isByRetryer() {
        return byRetryer;
    }

    public String getPreviousId() {
        return this.previousId;
    }

    public String getParentId() {
        return parentId;
    }

    public String getDataSourceName() {
        return this.dataSourceName;
    }

    public String getLastUpdatedUser() {
        return lastUpdatedUser;
    }

    public boolean isPersistent() {
        return this.persistent;
    }

    public boolean isInitialized() {
        return initialized;
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

    public void setStatus(int status) {
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

    public void setRecovered(boolean recovered) {
        this.recovered = recovered;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public void setByRetryer(boolean byRetryer) {
        this.byRetryer = byRetryer;
    }

    public void setPreviousId(String previousId) {
        this.previousId = previousId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public void setLastUpdatedUser(String lastUpdatedUser) {
        this.lastUpdatedUser = lastUpdatedUser;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    @Override
    public String toString() {
        return "TaskProperty(id=" + this.getId() + ", namespaceName=" + this.getNamespaceName() + ", appId=" + this.getAppId() + ", executorName=" + this.getExecutorName() + ", taskName=" + this.getTaskName() + ", taskExtraData=" + this.getTaskExtraData() + ", createTime=" + this.getCreateTime() + ", startTime=" + this.getStartTime() + ", endTime=" + this.getEndTime() + ", status=" + this.getStatus() + ", exitCode=" + this.getExitCode() + ", exitMessage=" + this.getExitMessage() + ", beanName=" + this.getBeanName() + ", beanTypeName=" + this.getBeanTypeName() + ", methodName=" + this.getMethodName() + ", methodParamTypes=" + this.getMethodParamTypes() + ", methodArguments=" + this.getMethodArguments() + ", recoverable=" + this.isRecoverable() + ", isRecovered=" + this.isRecovered() + ", host=" + this.getHost() + ", traceId=" + this.getTraceId() + ", byRetryer=" + this.isByRetryer() + ", previousId=" + this.getPreviousId() + ", parentId=" + this.getParentId() + ", dataSourceName=" + this.getDataSourceName() + ", lastUpdatedUser=" + this.getLastUpdatedUser() + ", persistent=" + this.isPersistent() + ", initialized=" + this.isInitialized() + ")";
    }

    public TaskProperty copy() {
        TaskProperty taskProperty = new TaskProperty();
        taskProperty.setId(this.id);
        taskProperty.setNamespaceName(this.namespaceName);
        taskProperty.setAppId(this.appId);
        taskProperty.setExecutorName(this.executorName);
        taskProperty.setTaskName(this.taskName);
        taskProperty.setTaskExtraData(this.taskExtraData);
        taskProperty.setCreateTime(this.createTime);
        taskProperty.setStartTime(this.startTime);
        taskProperty.setEndTime(this.endTime);
        taskProperty.setStatus(this.status);
        taskProperty.setExitCode(this.exitCode);
        taskProperty.setExitMessage(this.exitMessage);
        taskProperty.setBeanName(this.beanName);
        taskProperty.setBeanTypeName(this.beanTypeName);
        taskProperty.setMethodName(this.methodName);
        taskProperty.setMethodParamTypes(this.methodParamTypes);
        taskProperty.setMethodArguments(this.methodArguments);
        taskProperty.setRecoverable(this.recoverable);
        taskProperty.setRecovered(this.recovered);
        taskProperty.setHost(this.host);
        taskProperty.setTraceId(this.traceId);
        taskProperty.setByRetryer(this.byRetryer);
        taskProperty.setPreviousId(this.previousId);
        taskProperty.setParentId(this.parentId);
        taskProperty.setDataSourceName(this.dataSourceName);
        taskProperty.setLastUpdatedUser(this.lastUpdatedUser);
        taskProperty.setPersistent(this.persistent);
        taskProperty.setInitialized(this.initialized);

        return taskProperty;
    }
}
