package xyz.hellothomas.jedi.core.internals.executor;

import xyz.hellothomas.jedi.core.dto.consumer.ExecutorTaskNotification;
import xyz.hellothomas.jedi.core.internals.message.AbstractNotificationService;

/**
 * @author Thomas
 * @date 2021/1/15 16:37
 * @description
 * @version 1.0
 */
public class JediRunnable implements Runnable {
    private final String taskName;
    private final String taskExtraData;
    private final String poolName;
    private final Runnable runnable;
    private final AbstractNotificationService notificationService;

    public JediRunnable(JediThreadPoolExecutor executor, String taskName, Runnable runnable) {
        this.taskName = taskName;
        this.taskExtraData = null;
        this.poolName = executor.getPoolName();
        this.runnable = runnable;
        this.notificationService = executor.getNotificationService();
    }

    public JediRunnable(JediThreadPoolExecutor executor, String taskName, String taskExtraData, Runnable runnable) {
        this.taskName = taskName;
        this.taskExtraData = taskExtraData;
        this.poolName = executor.getPoolName();
        this.runnable = runnable;
        this.notificationService = executor.getNotificationService();
    }

    public JediRunnable(String poolName, String taskName, String taskExtraData,
                        AbstractNotificationService notificationService, Runnable runnable) {
        this.taskName = taskName;
        this.taskExtraData = taskExtraData;
        this.poolName = poolName;
        this.runnable = runnable;
        this.notificationService = notificationService;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        Throwable exception = null;
        try {
            runnable.run();
        } catch (Exception e) {
            exception = e;
            throw e;
        } finally {
            long diff = System.currentTimeMillis() - startTime;
            ExecutorTaskNotification executorTaskNotification =
                    notificationService.buildExecutorTaskNotification(taskName, taskExtraData, poolName, diff,
                            exception);
            notificationService.pushNotification(executorTaskNotification);
        }
    }

    public String getTaskName() {
        return taskName;
    }

    public String getPoolName() {
        return poolName;
    }
}
