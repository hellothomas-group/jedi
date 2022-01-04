package xyz.hellothomas.jedi.admin.infrastructure.listener;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import xyz.hellothomas.jedi.biz.common.enums.TaskOperationEnum;

@Getter
@Setter
public class TaskEvent extends ApplicationEvent {
    private TaskOperationEnum taskOperationEnum;

    public TaskEvent(Object source, TaskOperationEnum taskOperationEnum) {
        super(source);
        this.taskOperationEnum = taskOperationEnum;
    }
}
