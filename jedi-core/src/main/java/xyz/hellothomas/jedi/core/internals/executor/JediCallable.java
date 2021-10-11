package xyz.hellothomas.jedi.core.internals.executor;

import java.util.concurrent.Callable;

/**
 * @author Thomas
 * @date 2021/1/15 16:37
 * @description
 * @version 1.0
 */
public class JediCallable implements Callable {
    private final String taskName;
    private final String taskExtraData;
    private final JediThreadPoolExecutor executor;
    private final Callable callable;
    private final long submitTime;

    public JediCallable(Callable callable, JediThreadPoolExecutor executor, String taskName) {
        this.taskName = taskName;
        this.taskExtraData = null;
        this.executor = executor;
        this.callable = callable;
        this.submitTime = System.currentTimeMillis();
    }

    public JediCallable(Callable callable, JediThreadPoolExecutor executor, String taskName, String taskExtraData) {
        this.taskName = taskName;
        this.taskExtraData = taskExtraData;
        this.executor = executor;
        this.callable = callable;
        this.submitTime = System.currentTimeMillis();
    }

    @Override
    public Object call() throws Exception {
        // 替换默认的taskProperty
        long startTime = System.currentTimeMillis();
        TaskProperty taskProperty = new TaskProperty(taskName, taskExtraData, startTime - submitTime);
        taskProperty.setStartTime(startTime);
        executor.setTaskProperty(taskProperty);
        return callable.call();
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskExtraData() {
        return taskExtraData;
    }
}
