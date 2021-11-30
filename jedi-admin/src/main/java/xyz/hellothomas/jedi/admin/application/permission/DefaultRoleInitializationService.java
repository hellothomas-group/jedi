package xyz.hellothomas.jedi.admin.application.permission;

import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.hellothomas.jedi.admin.application.AppService;
import xyz.hellothomas.jedi.admin.common.enums.PermissonTypeEnum;
import xyz.hellothomas.jedi.admin.common.utils.RoleUtil;
import xyz.hellothomas.jedi.admin.domain.Executor;
import xyz.hellothomas.jedi.admin.domain.Permission;
import xyz.hellothomas.jedi.admin.domain.Role;
import xyz.hellothomas.jedi.biz.domain.BaseEntity;
import xyz.hellothomas.jedi.biz.domain.monitor.App;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Thomas
 * @date 2021/11/28 15:37
 * @description
 * @version 1.0
 */
@Service
public class DefaultRoleInitializationService implements RoleInitializationService {
    public static final String SYSTEM_MANAGER_ROLE_NAME =
            RoleUtil.buildSystemManagerRoleName();
    private final RolePermissionService rolePermissionService;
    private final AppService appService;

    public DefaultRoleInitializationService(RolePermissionService rolePermissionService, AppService appService) {
        this.rolePermissionService = rolePermissionService;
        this.appService = appService;
    }

    @Transactional
    @Override
    public void initSystemRoles() {
        if (rolePermissionService.findRoleByRoleName(SYSTEM_MANAGER_ROLE_NAME) != null) {
            return;
        }

        // permission
        Permission createOrDeleteAppPermission =
                rolePermissionService.findByPermissionTypeAndTargetId(PermissonTypeEnum.CREATE_DELETE_APP.getValue(),
                        "jedi");
        if (createOrDeleteAppPermission == null) {
            // create or delete application permission init
            createOrDeleteAppPermission = createPermission(PermissonTypeEnum.CREATE_DELETE_APP.getValue(),
                    "jedi", rolePermissionService.getSuperAdmin());
            rolePermissionService.createPermission(createOrDeleteAppPermission);
        }

        //  system manager role init
        Role systemManagerRole = createRole(SYSTEM_MANAGER_ROLE_NAME, rolePermissionService.getSuperAdmin());
        rolePermissionService.createRoleWithPermissions(systemManagerRole,
                Sets.newHashSet(createOrDeleteAppPermission.getId()));
    }

    @Transactional
    @Override
    public void initAppRoles(App app) {
        String appManagerRoleName = RoleUtil.buildAppManagerRoleName(app.getNamespaceName(), app.getAppId());

        //has created before
        if (rolePermissionService.findRoleByRoleName(appManagerRoleName) != null) {
            return;
        }

        String operator = app.getDataChangeCreatedBy();
        Set<Permission> appManagerPermissions =
                Stream.of(PermissonTypeEnum.GRANT_APP, PermissonTypeEnum.MODIFY_APP,
                        PermissonTypeEnum.CREATE_DELETE_EXECUTOR)
                        .map(permissionType -> createPermission(permissionType.getValue(),
                                String.valueOf(app.getId()), operator)).collect(Collectors.toSet());
        Set<Permission> createdAppManagerPermissions = rolePermissionService.createPermissions(appManagerPermissions);
        Set<Long> appManagerPermissionIds =
                createdAppManagerPermissions.stream().map(BaseEntity::getId).collect(Collectors.toSet());

        //create app manager role
        Role appManagerRole = createRole(appManagerRoleName, operator);

        rolePermissionService.createRoleWithPermissions(appManagerRole, appManagerPermissionIds);

        //assign APP_MANAGER role to user
        rolePermissionService
                .assignRoleToUsers(appManagerRoleName, Sets.newHashSet(app.getOwnerName()), operator);
    }

    @Transactional
    @Override
    public void initExecutorRoles(Executor executor) {
        String executorManagerRoleName = RoleUtil.buildExecutorManagerRoleName(executor.getNamespaceName(),
                executor.getAppId(), executor.getExecutorName());

        //has created before
        if (rolePermissionService.findRoleByRoleName(executorManagerRoleName) != null) {
            return;
        }

        String operator = executor.getDataChangeCreatedBy();
        Set<Permission> executorManagerPermissions =
                Stream.of(PermissonTypeEnum.GRANT_EXECUTOR, PermissonTypeEnum.MODIFY_EXECUTOR_CONFIG,
                        PermissonTypeEnum.RELEASE_EXECUTOR_CONFIG, PermissonTypeEnum.MODIFY_EXECUTOR_ALARM_CONFIG)
                        .map(permissionType -> createPermission(permissionType.getValue(),
                                String.valueOf(executor.getId()), operator)).collect(Collectors.toSet());
        Set<Permission> createdExecutorManagerPermissions =
                rolePermissionService.createPermissions(executorManagerPermissions);
        Set<Long> executorManagerPermissionIds =
                createdExecutorManagerPermissions.stream().map(BaseEntity::getId).collect(Collectors.toSet());

        //create app manager role
        Role executorManagerRole = createRole(executorManagerRoleName, operator);

        rolePermissionService.createRoleWithPermissions(executorManagerRole, executorManagerPermissionIds);

        //assign EXECUTOR_MANAGER role to user
        App app = appService.findOne(executor.getNamespaceName(), executor.getAppId());
        rolePermissionService
                .assignRoleToUsers(executorManagerRoleName, Sets.newHashSet(app.getOwnerName()), operator);
    }

    private Permission createPermission(String permissionType, String targetId, String operator) {
        Permission permission = new Permission();
        permission.setPermissionType(permissionType);
        permission.setTargetId(targetId);
        permission.setDataChangeCreatedBy(operator);
        permission.setDataChangeLastModifiedBy(operator);
        LocalDateTime currentTime = LocalDateTime.now();
        permission.setDataChangeCreatedTime(currentTime);
        permission.setDataChangeLastModifiedTime(currentTime);
        return permission;
    }

    private Role createRole(String roleName, String operator) {
        Role role = new Role();
        role.setRoleName(roleName);
        role.setDataChangeCreatedBy(operator);
        role.setDataChangeLastModifiedBy(operator);
        LocalDateTime currentTime = LocalDateTime.now();
        role.setDataChangeCreatedTime(currentTime);
        role.setDataChangeLastModifiedTime(currentTime);
        return role;
    }
}
