package xyz.hellothomas.jedi.admin.application.permission;

import xyz.hellothomas.jedi.admin.domain.Permission;
import xyz.hellothomas.jedi.admin.domain.Role;

import java.util.Set;

/**
 * @author Thomas
 * @date 2021/11/28 9:31
 * @description
 * @version 1.0
 */
public interface RolePermissionService {

    /**
     * Find role by role name, note that roleName should be unique
     */
    Role findRoleByRoleName(String roleName);

    /**
     * Create role with permissions, note that role name should be unique
     */
    Role createRoleWithPermissions(Role role, Set<Long> permissionIds);

    /**
     * Assign role to users
     *
     * @return the users assigned roles
     */
    Set<String> assignRoleToUsers(String roleName, Set<String> userIds,
                                  String operatorUserId);

    /**
     * Check whether user has the permission
     */
    boolean userHasPermission(String userId, String permissionType, String targetId);

    boolean isSuperAdmin(String userId);

    String getSuperAdmin();

    /**
     * Find permission by permissionType and targetId
     * @param permissionType
     * @param targetId
     * @return
     */
    Permission findByPermissionTypeAndTargetId(String permissionType, String targetId);

    /**
     * Create permission, note that permissionType + targetId should be unique
     */
    Permission createPermission(Permission permission);

    /**
     * Create permissions, note that permissionType + targetId should be unique
     */
    Set<Permission> createPermissions(Set<Permission> permissions);
}
