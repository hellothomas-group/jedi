package xyz.hellothomas.jedi.client.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.hellothomas.jedi.core.enums.TaskStatusEnum;
import xyz.hellothomas.jedi.core.internals.executor.AsyncAttributes;
import xyz.hellothomas.jedi.core.internals.executor.TaskProperty;
import xyz.hellothomas.jedi.core.utils.AsyncContextHolder;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

public class JediPersistentCallable<V> implements Callable<V> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JediPersistentCallable.class);
    private final Callable<V> callable;
    private final AsyncAttributes asyncAttributes;
    private final TaskProperty taskProperty;
    private final PersistenceService persistenceService;

    public JediPersistentCallable(Callable<V> callable, PersistenceService persistenceService) {
        this.callable = callable;
        this.asyncAttributes = AsyncContextHolder.getAsyncAttributes();
        this.taskProperty = (TaskProperty) asyncAttributes.getAttribute(TaskProperty.class.getName());
        this.persistenceService = persistenceService;
    }

    @Override
    public V call() throws Exception {
        if (taskProperty.getStartTime() == null) {
            // 任务开始
            taskProperty.setStartTime(LocalDateTime.now());
            taskProperty.setStatus(TaskStatusEnum.DOING.getValue());

            AsyncAttributes asyncAttributes = new AsyncAttributes();
            asyncAttributes.setAttribute(TaskProperty.class.getName(), taskProperty);
            AsyncContextHolder.setAsyncAttributes(asyncAttributes);
        }

        try {
            // 任务开始持久化
            persistenceService.updateTaskExecution(taskProperty);
            V result = callable.call();
            // 任务成功持久化
            persistenceService.deleteTaskExecution(taskProperty);
            return result;
        } catch (Exception e) {
            if (taskProperty.getEndTime() == null) {
                taskProperty.setEndTime(LocalDateTime.now());
                taskProperty.setStatus(TaskStatusEnum.FAIL.getValue());
                LOGGER.error(String.format("taskId:%s, taskName：%s, 执行异常!", taskProperty.getId(),
                        taskProperty.getTaskName()), e);
                String exceptionString = e.getMessage();
                if (exceptionString != null) {
                    exceptionString = exceptionString.length() > 300 ? exceptionString.substring(0,
                            300) : exceptionString;
                    taskProperty.setExitMessage(exceptionString);
                }
            }
            // 任务异常持久化
            persistenceService.updateTaskExecution(taskProperty);
            throw e;
        }
    }
}
