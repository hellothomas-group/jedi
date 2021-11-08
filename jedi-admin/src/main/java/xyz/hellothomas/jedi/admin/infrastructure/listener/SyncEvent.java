package xyz.hellothomas.jedi.admin.infrastructure.listener;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import xyz.hellothomas.jedi.admin.common.enums.SyncOperationEnum;
import xyz.hellothomas.jedi.admin.common.enums.SyncTypeEnum;

@Getter
@Setter
public class SyncEvent extends ApplicationEvent {
    private SyncTypeEnum syncTypeEnum;
    private SyncOperationEnum syncOperationEnum;

    public SyncEvent(Object source, SyncTypeEnum syncTypeEnum, SyncOperationEnum syncOperationEnum) {
        super(source);
        this.syncTypeEnum = syncTypeEnum;
        this.syncOperationEnum = syncOperationEnum;
    }
}
