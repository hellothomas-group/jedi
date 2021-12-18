package xyz.hellothomas.jedi.core.internals.executor;

import java.util.concurrent.Callable;

/**
 * @author Thomas
 * @date 2021/1/15 16:37
 * @description
 * @version 1.0
 */
public class JediCallable<V> implements Callable<V> {
    private final String taskName;
    private final String taskExtraData;
    private final JediThreadPoolExecutor executor;
    private final Callable<V> callable;
    private final long submitTime;
    private final String id;

    public JediCallable(JediThreadPoolExecutor executor, String taskName, Callable<V> callable) {
        this.taskName = taskName;
        this.taskExtraData = null;
        this.executor = executor;
        this.callable = callable;
        this.submitTime = System.currentTimeMillis();
        this.id = null;
    }

    public JediCallable(JediThreadPoolExecutor executor, String taskName, String taskExtraData, Callable<V> callable) {
        this.taskName = taskName;
        this.taskExtraData = taskExtraData;
        this.executor = executor;
        this.callable = callable;
        this.submitTime = System.currentTimeMillis();
        this.id = null;
    }

    public JediCallable(JediThreadPoolExecutor executor, String taskName, String taskExtraData, Callable<V> callable,
                        String id) {
        this.taskName = taskName;
        this.taskExtraData = taskExtraData;
        this.executor = executor;
        this.callable = callable;
        this.submitTime = System.currentTimeMillis();
        this.id = id;
    }

    @Override
    public V call() throws Exception {
        // 替换默认的taskProperty
        long startTime = System.currentTimeMillis();
        TaskProperty taskProperty = new TaskProperty(taskName, taskExtraData, startTime - submitTime, id);
        taskProperty.setStartTime(startTime);
        executor.setTaskProperty(taskProperty);
        return (V) callable.call();
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskExtraData() {
        return taskExtraData;
    }
}
