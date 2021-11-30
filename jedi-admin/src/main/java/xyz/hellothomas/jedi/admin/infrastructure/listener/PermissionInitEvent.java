package xyz.hellothomas.jedi.admin.infrastructure.listener;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import xyz.hellothomas.jedi.admin.common.enums.RoleTypeEnum;

/**
 * @author 80234613 唐圆
 * @date 2021/11/30 14:09
 * @descripton
 * @version 1.0
 */
@Getter
@Setter
public class PermissionInitEvent extends ApplicationEvent {
    private RoleTypeEnum roleTypeEnum;

    /**
     * Create a new {@code ApplicationEvent}.
     * @param source the object on which the event initially occurred or with
     * which the event is associated (never {@code null})
     */
    public PermissionInitEvent(Object source, RoleTypeEnum roleTypeEnum) {
        super(source);
        this.roleTypeEnum = roleTypeEnum;
    }
}
