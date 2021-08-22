package xyz.hellothomas.jedi.core.internals.message;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.hellothomas.jedi.core.dto.consumer.*;
import xyz.hellothomas.jedi.core.enums.MessageType;
import xyz.hellothomas.jedi.core.internals.executor.JediThreadPoolExecutor;
import xyz.hellothomas.jedi.core.utils.NetUtil;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Thomas
 * @date 2021/1/23 21:35
 * @description
 * @version 1.0
 */
public abstract class AbstractNotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNotificationService.class);

    /**
     * 主机地址
     */
    protected String host = NetUtil.getLocalHost();

    /**
     * appId
     */
    protected String appId;

    /**
     * namespace
     */
    protected String namespace;

    public AbstractNotificationService(String appId, String namespace) {
        this.appId = appId;
        this.namespace = namespace;
    }

    public ExecutorTickerNotification buildExecutorTickerNotification(JediThreadPoolExecutor executor) {
        ExecutorTickerNotification executorTickerNotification = new ExecutorTickerNotification();
        executorTickerNotification.setPoolName(executor.getPoolName());
        executorTickerNotification.setCorePoolSize(executor.getCorePoolSize());
        executorTickerNotification.setMaximumPoolSize(executor.getMaximumPoolSize());
        executorTickerNotification.setPoolSize(executor.getPoolSize());
        executorTickerNotification.setActiveCount(executor.getActiveCount());
        executorTickerNotification.setLargestPoolSize(executor.getLargestPoolSize());

        executorTickerNotification.setQueueType(executor.getQueue().getClass().getSimpleName());
        executorTickerNotification.setQueueSize(executor.getQueue().size());
        executorTickerNotification.setQueueRemainingCapacity(executor.getQueue().remainingCapacity());

        executorTickerNotification.setTaskCount(executor.getTaskCount());
        executorTickerNotification.setCompletedTaskCount(executor.getCompletedTaskCount());
        executorTickerNotification.setRejectCount(executor.getRejectCount());
        executorTickerNotification.setLastRejectCount(executor.getLastRejectCount());

        executor.setLastRejectCount(executorTickerNotification.getRejectCount());

        executorTickerNotification.setIsShutdown(executor.isShutdown());
        executorTickerNotification.setIsTerminated(executor.isTerminated());

        executorTickerNotification.setMessageType(MessageType.EXECUTOR_TICKER.getTypeValue());
        fillMessageCommonField(executorTickerNotification);

        LOGGER.trace("build tickerMessage:{}", executorTickerNotification);

        return executorTickerNotification;
    }

    public ExecutorTaskNotification buildExecutorTaskNotification(String taskName, String taskExtraData,
                                                                  String poolName, long executionTime, Throwable t) {
        ExecutorTaskNotification executorTaskNotification = new ExecutorTaskNotification();
        if (StringUtils.isNotBlank(taskName)) {
            executorTaskNotification.setTaskName(taskName);
        }
        if (StringUtils.isNotBlank(taskExtraData)) {
            executorTaskNotification.setTaskExtraData(taskExtraData);
        }
        executorTaskNotification.setExecutionTime(executionTime);
        executorTaskNotification.setPoolName(poolName);
        if (t == null) {
            executorTaskNotification.setIsSuccess(true);
        } else {
            executorTaskNotification.setIsSuccess(false);
            String exceptionString = t.toString();
            executorTaskNotification.setFailureReason(exceptionString.length() > 300 ? exceptionString.substring(0,
                    300) : exceptionString);
        }

        executorTaskNotification.setMessageType(MessageType.EXECUTOR_TASK.getTypeValue());
        fillMessageCommonField(executorTaskNotification);

        LOGGER.trace("build taskMessage:{}", executorTaskNotification);

        return executorTaskNotification;
    }

    public ExecutorShutdownNotification buildExecutorShutdownNotification(String poolName, long completedTaskCount,
                                                                          long executingTaskCount,
                                                                          long toExecuteTaskCount) {
        ExecutorShutdownNotification executorShutdownNotification = new ExecutorShutdownNotification();
        executorShutdownNotification.setPoolName(poolName);
        executorShutdownNotification.setCompletedTaskCount(completedTaskCount);
        executorShutdownNotification.setExecutingTaskCount(executingTaskCount);
        executorShutdownNotification.setToExecuteTaskCount(toExecuteTaskCount);

        executorShutdownNotification.setMessageType(MessageType.EXECUTOR_SHUTDOWN.getTypeValue());
        fillMessageCommonField(executorShutdownNotification);

        LOGGER.trace("build executorShutdownNotification:{}", executorShutdownNotification);

        return executorShutdownNotification;
    }

    public CustomNotification buildCustomNotification(String content) {
        CustomNotification customNotification = new CustomNotification();
        customNotification.setMessageType("testMessageType");
        customNotification.setContent(content);
        fillMessageCommonField(customNotification);

        LOGGER.trace("build customNotification:{}", customNotification);

        return customNotification;
    }

    public abstract void pushNotification(AbstractNotification notification);

    public abstract void send(Object request, MessageType messageType);

    private void fillMessageCommonField(AbstractNotification notification) {
        notification.setId(UUID.randomUUID().toString());
        notification.setAppId(appId);
        notification.setNamespace(namespace);
        notification.setHost(host);
        notification.setRecordTime(LocalDateTime.now());
    }
}
