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
    private final long submitTime;

    public JediRunnable(JediThreadPoolExecutor executor, String taskName, Runnable runnable) {
        this.taskName = taskName;
        this.taskExtraData = null;
        this.executor = executor;
        this.runnable = runnable;
        this.submitTime = System.currentTimeMillis();
    }

    public JediRunnable(JediThreadPoolExecutor executor, String taskName, String taskExtraData, Runnable runnable) {
        this.taskName = taskName;
        this.taskExtraData = taskExtraData;
        this.executor = executor;
        this.runnable = runnable;
        this.submitTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        // 替换默认的taskProperty
        long startTime = System.currentTimeMillis();
        TaskProperty taskProperty = new TaskProperty(taskName, taskExtraData, startTime - submitTime);
        taskProperty.setStartTime(startTime);
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
