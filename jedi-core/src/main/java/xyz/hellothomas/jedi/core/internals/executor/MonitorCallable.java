package xyz.hellothomas.jedi.core.internals.executor;

import xyz.hellothomas.jedi.core.dto.consumer.ExecutorTaskNotification;
import xyz.hellothomas.jedi.core.internals.message.AbstractNotificationService;

import java.util.concurrent.Callable;

/**
 * @author Thomas
 * @date 2021/1/15 16:37
 * @description
 * @version 1.0
 */
public class MonitorCallable implements Callable {
    private final String taskName;
    private final String poolName;
    private final Callable callable;
    private final AbstractNotificationService notificationService;

    public MonitorCallable(Callable callable, DynamicThreadPoolExecutor executor, String taskName) {
        this.taskName = taskName;
        this.poolName = executor.getPoolName();
        this.callable = callable;
        this.notificationService = executor.getNotificationService();
    }

    public MonitorCallable(Callable callable, String poolName, String taskName,
                           AbstractNotificationService notificationService) {
        this.taskName = taskName;
        this.poolName = poolName;
        this.callable = callable;
        this.notificationService = notificationService;
    }

    @Override
    public Object call() throws Exception {
        Long startTime = System.nanoTime();
        try {
            return callable.call();
        } finally {
            long diff = System.nanoTime() - startTime;
            ExecutorTaskNotification executorTaskNotification =
                    notificationService.buildExecutorTaskNotification(taskName
                            , poolName, diff);
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
