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
    private Long startTime;

    public TaskProperty(String taskName, String taskExtraData) {
        this.taskName = taskName;
        this.taskExtraData = taskExtraData;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskExtraData() {
        return taskExtraData;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }
}
