package com.hellothomas.jedi.core.internals.executor;

import com.hellothomas.jedi.core.dto.consumer.ExecutorTaskNotification;
import com.hellothomas.jedi.core.internals.message.AbstractNotificationService;

/**
 * @className MonitorRunnable
 * @author Thomas
 * @date 2021/1/15 16:37
 * @description
 * @version 1.0
 */
public class MonitorRunnable implements Runnable {
    private final String taskName;
    private final String poolName;
    private final Runnable runnable;
    private final AbstractNotificationService notificationService;

    public MonitorRunnable(DynamicThreadPoolExecutor executor, String taskName, Runnable runnable) {
        this.taskName = taskName;
        this.poolName = executor.getPoolName();
        this.runnable = runnable;
        this.notificationService = executor.getNotificationService();
    }

    public MonitorRunnable(String poolName, String taskName, AbstractNotificationService notificationService,
                           Runnable runnable) {
        this.taskName = taskName;
        this.poolName = poolName;
        this.runnable = runnable;
        this.notificationService = notificationService;
    }

    @Override
    public void run() {
        long startTime = System.nanoTime();
        try {
            runnable.run();
        } finally {
            long diff = System.nanoTime() - startTime;
            ExecutorTaskNotification executorTaskNotification =
                    notificationService.buildExecutorTaskNotification(taskName, poolName, diff);
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
