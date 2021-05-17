package xyz.hellothomas.jedi.core.internals.message;

import xyz.hellothomas.jedi.core.dto.consumer.*;
import xyz.hellothomas.jedi.core.enums.MessageType;
import xyz.hellothomas.jedi.core.internals.executor.DynamicThreadPoolExecutor;

/**
 * @author Thomas
 * @date 2021/4/13 23:18
 * @description
 * @version 1.0
 */
public class NullNotificationService extends AbstractNotificationService {
    public NullNotificationService(String appId, String namespace) {
        super(appId, namespace);
    }

    @Override
    public void pushNotification(AbstractNotification notification) {

    }

    @Override
    public void send(Object request, MessageType messageType) {

    }

    @Override
    public ExecutorTickerNotification buildExecutorTickerNotification(DynamicThreadPoolExecutor executor) {
        return null;
    }

    @Override
    public ExecutorTaskNotification buildExecutorTaskNotification(String taskName, String poolName,
                                                                  long executionTime) {
        return null;
    }

    @Override
    public ExecutorShutdownNotification buildExecutorShutdownNotification(String poolName, long completedTaskCount,
                                                                          long executingTaskCount,
                                                                          long toExecuteTaskCount) {
        return null;
    }

    @Override
    public CustomNotification buildCustomNotification(String content) {
        return null;
    }
}
