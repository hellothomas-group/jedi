package xyz.hellothomas.jedi.admin.application.permission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.hellothomas.jedi.admin.application.AppService;
import xyz.hellothomas.jedi.admin.application.ExecutorService;
import xyz.hellothomas.jedi.admin.common.enums.PermissonTypeEnum;
import xyz.hellothomas.jedi.admin.domain.Executor;
import xyz.hellothomas.jedi.biz.domain.monitor.App;

import static xyz.hellothomas.jedi.admin.application.permission.DefaultRoleInitializationService.SYSTEM_MANAGER_TARGET_ID;

@Component("permissionValidator")
public class PermissionValidator {

    private final AppService appService;
    private final ExecutorService executorService;
    private final RolePermissionService rolePermissionService;


    @Autowired
    public PermissionValidator(
            AppService appService, ExecutorService executorService, final RolePermissionService rolePermissionService) {
        this.appService = appService;
        this.executorService = executorService;
        this.rolePermissionService = rolePermissionService;
    }

    public boolean hasCreateOrDeleteAppPermission(String operator) {
        return rolePermissionService.userHasPermission(operator, PermissonTypeEnum.CREATE_DELETE_APP.getValue(),
                SYSTEM_MANAGER_TARGET_ID);
    }

    public boolean hasGrantAppPermission(String namespaceName, String appId, String operator) {
        App app = appService.findOne(namespaceName, appId);
        return rolePermissionService.userHasPermission(operator, PermissonTypeEnum.GRANT_APP.getValue(),
                String.valueOf(app.getId()));
    }

    public boolean hasModifyAppPermission(String namespaceName, String appId, String operator) {
        App app = appService.findOne(namespaceName, appId);
        return rolePermissionService.userHasPermission(operator, PermissonTypeEnum.MODIFY_APP.getValue(),
                String.valueOf(app.getId()));
    }

    public boolean hasCreateOrDeleteExecutorPermission(String namespaceName, String appId, String operator) {
        App app = appService.findOne(namespaceName, appId);
        return rolePermissionService.userHasPermission(operator, PermissonTypeEnum.CREATE_DELETE_EXECUTOR.getValue(),
                String.valueOf(app.getId()));
    }

    public boolean hasModifyExecutorAlarmConfigPermission(String namespaceName, String appId,
                                                          String executorName, String operator) {
        Executor executor = executorService.findOne(namespaceName, appId, executorName);
        return rolePermissionService.userHasPermission(operator,
                PermissonTypeEnum.MODIFY_EXECUTOR_ALARM_CONFIG.getValue(),
                String.valueOf(executor.getId()));
    }

    public boolean hasReleaseExecutorConfigPermission(String namespaceName, String appId, String executorName,
                                                      String operator) {
        Executor executor = executorService.findOne(namespaceName, appId, executorName);
        return rolePermissionService.userHasPermission(operator,
                PermissonTypeEnum.RELEASE_EXECUTOR_CONFIG.getValue(),
                String.valueOf(executor.getId()));
    }

    public boolean hasModifyExecutorConfigPermission(String namespaceName, String appId, String executorName,
                                                     String operator) {
        Executor executor = executorService.findOne(namespaceName, appId, executorName);
        return rolePermissionService.userHasPermission(operator,
                PermissonTypeEnum.MODIFY_EXECUTOR_CONFIG.getValue(),
                String.valueOf(executor.getId()));
    }

    public boolean hasGrantExecutorPermission(String namespaceName, String appId, String executorName,
                                              String operator) {
        Executor executor = executorService.findOne(namespaceName, appId, executorName);
        return rolePermissionService.userHasPermission(operator,
                PermissonTypeEnum.GRANT_EXECUTOR.getValue(),
                String.valueOf(executor.getId()));
    }

    public boolean test() {
        return false;
    }
}
