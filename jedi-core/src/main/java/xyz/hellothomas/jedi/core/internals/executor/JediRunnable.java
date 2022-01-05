package xyz.hellothomas.jedi.core.internals.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.hellothomas.jedi.core.enums.TaskStatusEnum;
import xyz.hellothomas.jedi.core.utils.AsyncContextHolder;

import java.time.LocalDateTime;

/**
 * @author Thomas
 * @date 2021/1/15 16:37
 * @description
 * @version 1.0
 */
public class JediRunnable implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(JediRunnable.class);
    private final Runnable runnable;
    private final TaskProperty taskProperty;

    public JediRunnable(Runnable runnable, TaskProperty taskProperty) {
        this.runnable = runnable;
        this.taskProperty = taskProperty.copy();
    }

    @Override
    public void run() {
        if (taskProperty.getStartTime() == null) {
            // 任务开始
            taskProperty.setStartTime(LocalDateTime.now());
            taskProperty.setStatus(TaskStatusEnum.DOING.getValue());
            LOGGER.trace("TaskProperty:{}", taskProperty);

            AsyncAttributes asyncAttributes = new AsyncAttributes();
            asyncAttributes.setAttribute(TaskProperty.class.getName(), taskProperty);
            AsyncContextHolder.setAsyncAttributes(asyncAttributes);
        }

        try {
            runnable.run();
            if (taskProperty.getEndTime() == null) {
                // 任务成功
                taskProperty.setEndTime(LocalDateTime.now());
                taskProperty.setStatus(TaskStatusEnum.SUCCESS.getValue());
                LOGGER.trace("TaskProperty:{}", taskProperty);
            }
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
                LOGGER.trace("TaskProperty:{}", taskProperty);
            }
            throw e;
        }
    }
}
