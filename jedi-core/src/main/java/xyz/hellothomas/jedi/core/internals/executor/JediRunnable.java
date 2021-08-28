package xyz.hellothomas.jedi.core.internals.executor;

/**
 * @author Thomas
 * @date 2021/1/15 16:37
 * @description
 * @version 1.0
 */
public class JediRunnable implements Runnable {
    private final String taskName;
    private final String taskExtraData;
    private final JediThreadPoolExecutor executor;
    private final Runnable runnable;

    public JediRunnable(JediThreadPoolExecutor executor, String taskName, Runnable runnable) {
        this.taskName = taskName;
        this.taskExtraData = null;
        this.executor = executor;
        this.runnable = runnable;
    }

    public JediRunnable(JediThreadPoolExecutor executor, String taskName, String taskExtraData, Runnable runnable) {
        this.taskName = taskName;
        this.taskExtraData = taskExtraData;
        this.executor = executor;
        this.runnable = runnable;
    }

    @Override
    public void run() {
        // 替换默认的taskProperty
        TaskProperty taskProperty = new TaskProperty(taskName, taskExtraData);
        taskProperty.setStartTime(System.currentTimeMillis());
        executor.setTaskProperty(taskProperty);
        runnable.run();
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskExtraData() {
        return taskExtraData;
    }
}
