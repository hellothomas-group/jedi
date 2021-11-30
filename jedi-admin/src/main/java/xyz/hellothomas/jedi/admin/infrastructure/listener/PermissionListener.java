package xyz.hellothomas.jedi.admin.infrastructure.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import xyz.hellothomas.jedi.admin.application.permission.RoleInitializationService;
import xyz.hellothomas.jedi.admin.common.enums.RoleTypeEnum;
import xyz.hellothomas.jedi.admin.domain.Executor;
import xyz.hellothomas.jedi.biz.domain.monitor.App;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class PermissionListener {
    private final RoleInitializationService roleInitializationService;

    public PermissionListener(RoleInitializationService roleInitializationService) {
        this.roleInitializationService = roleInitializationService;
    }

    @Async
    @EventListener
    public void onInitPermissionEvent(PermissionInitEvent event) {
        try {
            if (RoleTypeEnum.APP_MANAGER == event.getRoleTypeEnum()) {
                roleInitializationService.initAppRoles((App) event.getSource());
            } else if (RoleTypeEnum.EXECUTOR_MANAGER == event.getRoleTypeEnum()) {
                roleInitializationService.initExecutorRoles((Executor) event.getSource());
            }
            log.info("Init success. roleType = {}, object = {})", event.getRoleTypeEnum(), event.getSource());
        } catch (Exception e) {
            log.error("Init failed. roleType = {}, object = {})", event.getRoleTypeEnum(), event.getSource(), e);
        }
    }

    @PostConstruct
    public void init() {
        roleInitializationService.initSystemRoles();
        log.info("Init systemRoles success");
    }
}
