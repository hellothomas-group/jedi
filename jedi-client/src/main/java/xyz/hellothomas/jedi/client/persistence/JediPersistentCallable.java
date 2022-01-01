package xyz.hellothomas.jedi.client.persistence;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import xyz.hellothomas.jedi.core.enums.TaskStatusEnum;
import xyz.hellothomas.jedi.core.internals.executor.AsyncAttributes;
import xyz.hellothomas.jedi.core.internals.executor.TaskProperty;
import xyz.hellothomas.jedi.core.utils.AsyncContextHolder;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

@Slf4j
public class JediPersistentCallable<V> implements Callable<V> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JediPersistentCallable.class);
    private final Callable<V> callable;
    private final PersistenceService persistenceService;
    private final TaskProperty taskProperty = new TaskProperty();

    public JediPersistentCallable(Callable<V> callable, PersistenceService persistenceService) {
        this.callable = callable;
        this.persistenceService = persistenceService;
        TaskProperty originTaskProperty =
                (TaskProperty) AsyncContextHolder.getAsyncAttributes().getAttribute(TaskProperty.class.getName());
        BeanUtils.copyProperties(originTaskProperty, taskProperty);
    }

    @Override
    public V call() throws Exception {
        if (taskProperty.getStartTime() == null) {
            // 任务开始
            taskProperty.setStartTime(LocalDateTime.now());
            taskProperty.setStatus(TaskStatusEnum.DOING.getValue());
            log.trace("TaskProperty:{}", taskProperty);

            AsyncAttributes asyncAttributes = new AsyncAttributes();
            asyncAttributes.setAttribute(TaskProperty.class.getName(), taskProperty);
            AsyncContextHolder.setAsyncAttributes(asyncAttributes);
        }
        // 任务开始持久化
        persistenceService.updateTaskExecution(taskProperty);

        try {
            V result = callable.call();
            if (taskProperty.getEndTime() == null) {
                // 任务成功
                taskProperty.setEndTime(LocalDateTime.now());
                taskProperty.setStatus(TaskStatusEnum.SUCCESS.getValue());
                log.trace("TaskProperty:{}", taskProperty);
            }
            // 任务成功持久化
            persistenceService.deleteTaskExecution(taskProperty);
            return result;
        } catch (Exception e) {
            if (taskProperty.getEndTime() == null) {
                // 任务失败
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
                log.trace("TaskProperty:{}", taskProperty);
            }
            // 任务异常持久化
            persistenceService.updateTaskExecution(taskProperty);
            throw e;
        }
    }
}
