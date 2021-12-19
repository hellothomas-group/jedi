package xyz.hellothomas.jedi.core.internals.executor;

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
    private final Runnable runnable;
    private final AsyncAttributes asyncAttributes;
    private final TaskProperty taskProperty;

    public JediRunnable(Runnable runnable) {
        this.runnable = runnable;
        this.asyncAttributes = AsyncContextHolder.getAsyncAttributes();
        this.taskProperty = (TaskProperty) asyncAttributes.getAttribute(TaskProperty.class.getName());
    }

    @Override
    public void run() {
        if (taskProperty.getStartTime() == null) {
            // 任务开始
            taskProperty.setStartTime(LocalDateTime.now());
            taskProperty.setStatus(TaskStatusEnum.DOING.getValue());

            AsyncAttributes asyncAttributes = new AsyncAttributes();
            asyncAttributes.setAttribute(TaskProperty.class.getName(), taskProperty);
            AsyncContextHolder.setAsyncAttributes(asyncAttributes);
        }

        runnable.run();
    }
}
