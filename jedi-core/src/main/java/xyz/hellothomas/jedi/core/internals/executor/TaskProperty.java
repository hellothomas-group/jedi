package xyz.hellothomas.jedi.core.internals.executor;

/**
 * @author Thomas
 * @date 2021/8/28 14:32
 * @description
 * @version 1.0
 */
public class TaskProperty {
    private final String taskName;
    private final String taskExtraData;
    private final long waitTime;
    private long startTime;
    private String id;

    public TaskProperty(String taskName, String taskExtraData, long waitTime) {
        this.taskName = taskName;
        this.taskExtraData = taskExtraData;
        this.waitTime = waitTime;
        this.id = null;
    }

    public TaskProperty(String taskName, String taskExtraData, long waitTime, String id) {
        this.taskName = taskName;
        this.taskExtraData = taskExtraData;
        this.waitTime = waitTime;
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskExtraData() {
        return taskExtraData;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public String getId() {
        return id;
    }
}
