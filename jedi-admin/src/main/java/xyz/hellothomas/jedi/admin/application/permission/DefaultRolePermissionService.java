package xyz.hellothomas.jedi.admin.application.permission;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import xyz.hellothomas.jedi.admin.domain.*;
import xyz.hellothomas.jedi.admin.infrastructure.mapper.PermissionMapper;
import xyz.hellothomas.jedi.admin.infrastructure.mapper.RoleMapper;
import xyz.hellothomas.jedi.admin.infrastructure.mapper.RolePermissionMapper;
import xyz.hellothomas.jedi.admin.infrastructure.mapper.UserRoleMapper;
import xyz.hellothomas.jedi.biz.common.utils.LocalBeanUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Thomas
 * @date 2021/11/27 17:04
 * @description
 * @version 1.0
 */
@Service
public class DefaultRolePermissionService implements RolePermissionService {
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;
    private final UserRoleMapper userRoleMapper;
    private final RolePermissionMapper rolePermissionMapper;

    @Value("${admin-service.super-admin}")
    private String superAdmin;

    public DefaultRolePermissionService(RoleMapper roleMapper, PermissionMapper permissionMapper,
                                        UserRoleMapper userRoleMapper, RolePermissionMapper rolePermissionMapper) {
        this.roleMapper = roleMapper;
        this.permissionMapper = permissionMapper;
        this.userRoleMapper = userRoleMapper;
        this.rolePermissionMapper = rolePermissionMapper;
    }

    @Override
    public Role findRoleByRoleName(String roleName) {
        RoleExample roleExample = new RoleExample();
        roleExample.createCriteria().andRoleNameEqualTo(roleName);
        List<Role> roles = roleMapper.selectByExample(roleExample);
        return roles.isEmpty() ? null : roles.get(0);
    }

    @Transactional
    @Override
    public Role createRoleWithPermissions(Role role, Set<Long> permissionIds) {
        Role current = findRoleByRoleName(role.getRoleName());
        Preconditions.checkState(current == null, "Role %s already exists!", role.getRoleName());

        Role createdRole = LocalBeanUtils.transform(Role.class, role);
        roleMapper.insert(createdRole);

        if (!CollectionUtils.isEmpty(permissionIds)) {
            List<RolePermission> rolePermissions = permissionIds.stream().map(permissionId -> {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(createdRole.getId());
                rolePermission.setPermissionId(permissionId);
                rolePermission.setDataChangeCreatedBy(createdRole.getDataChangeCreatedBy());
                rolePermission.setDataChangeLastModifiedBy(createdRole.getDataChangeLastModifiedBy());
                LocalDateTime currentTime = LocalDateTime.now();
                rolePermission.setDataChangeCreatedTime(currentTime);
                rolePermission.setDataChangeLastModifiedTime(currentTime);
                return rolePermission;
            }).collect(Collectors.toList());
            rolePermissionMapper.insertBatch(rolePermissions);
        }

        return createdRole;
    }

    @Transactional
    @Override
    public Set<String> assignRoleToUsers(String roleName, Set<String> userIds, String operatorUserId) {
        Role role = findRoleByRoleName(roleName);
        Preconditions.checkState(role != null, "Role %s doesn't exist!", roleName);

        List<UserRole> existedUserRoles = findByUserIdInAndRoleId(new ArrayList<>(userIds), role.getId());
        Set<String> existedUserIds =
                existedUserRoles.stream().map(UserRole::getUserId).collect(Collectors.toSet());

        Set<String> toAssignUserIds = Sets.difference(userIds, existedUserIds);

        List<UserRole> toCreate = toAssignUserIds.stream().map(userId -> {
            UserRole userRole = new UserRole();
            userRole.setRoleId(role.getId());
            userRole.setUserId(userId);
            userRole.setDataChangeCreatedBy(operatorUserId);
            userRole.setDataChangeLastModifiedBy(operatorUserId);
            LocalDateTime currentTime = LocalDateTime.now();
            userRole.setDataChangeCreatedTime(currentTime);
            userRole.setDataChangeLastModifiedTime(currentTime);
            return userRole;
        }).collect(Collectors.toList());

        userRoleMapper.insertBatch(toCreate);
        return toAssignUserIds;
    }

    @Override
    public boolean userHasPermission(String userId, String permissionType, String targetId) {
        Permission permission = findByPermissionTypeAndTargetId(permissionType, targetId);
        if (permission == null) {
            return false;
        }

        if (isSuperAdmin(userId)) {
            return true;
        }

        List<UserRole> userRoles = findByUserId(userId);
        if (userRoles == null || userRoles.isEmpty()) {
            return false;
        }

        Set<Long> roleIds =
                userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
        List<RolePermission> rolePermissions = findByRoleIdIn(new ArrayList<>(roleIds));
        if (rolePermissions == null || rolePermissions.isEmpty()) {
            return false;
        }

        for (RolePermission rolePermission : rolePermissions) {
            if (rolePermission.getPermissionId().longValue() == permission.getId().longValue()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isSuperAdmin(String userId) {
        return false;
    }

    @Override
    public String getSuperAdmin() {
        return superAdmin;
    }

    @Override
    public Permission findByPermissionTypeAndTargetId(String permissionType, String targetId) {
        PermissionExample permissionExample = new PermissionExample();
        permissionExample.createCriteria().andPermissionTypeEqualTo(permissionType)
                .andTargetIdEqualTo(targetId);
        List<Permission> permissions = permissionMapper.selectByExample(permissionExample);
        return permissions.isEmpty() ? null : permissions.get(0);
    }

    @Override
    public Permission createPermission(Permission permission) {
        Permission createdPermission = LocalBeanUtils.transform(Permission.class, permission);
        permissionMapper.insert(permission);
        return createdPermission;
    }

    @Override
    public Set<Permission> createPermissions(Set<Permission> permissions) {
        Multimap<String, String> targetIdPermissionTypes = HashMultimap.create();
        for (Permission permission : permissions) {
            targetIdPermissionTypes.put(permission.getTargetId(), permission.getPermissionType());
        }

        for (String targetId : targetIdPermissionTypes.keySet()) {
            Collection<String> permissionTypes = targetIdPermissionTypes.get(targetId);
            List<Permission> current = findByPermissionTypeInAndTargetId(new ArrayList<>(permissionTypes), targetId);
            Preconditions.checkState(CollectionUtils.isEmpty(current),
                    "Permission with permissionType %s targetId %s already exists!", permissionTypes,
                    targetId);
        }

        List<Permission> createdPermissions = new ArrayList<>(permissions);
        permissionMapper.insertBatch(createdPermissions);
        return new HashSet<>(createdPermissions);
    }

    private List<Permission> findByPermissionTypeInAndTargetId(List<String> permissionTypes, String targetId) {
        PermissionExample permissionExample = new PermissionExample();
        permissionExample.createCriteria().andTargetIdEqualTo(targetId)
                .andPermissionTypeIn(permissionTypes);
        return permissionMapper.selectByExample(permissionExample);
    }

    private List<UserRole> findByUserIdInAndRoleId(List<String> userIds, Long roleId) {
        UserRoleExample userRoleExample = new UserRoleExample();
        userRoleExample.createCriteria().andRoleIdEqualTo(roleId)
                .andUserIdIn(userIds);
        return userRoleMapper.selectByExample(userRoleExample);
    }

    private List<UserRole> findByUserId(String userId) {
        UserRoleExample userRoleExample = new UserRoleExample();
        userRoleExample.createCriteria().andUserIdEqualTo(userId);
        return userRoleMapper.selectByExample(userRoleExample);
    }

    private List<RolePermission> findByRoleIdIn(List<Long> roleIds) {
        RolePermissionExample rolePermissionExample = new RolePermissionExample();
        rolePermissionExample.createCriteria().andRoleIdIn(roleIds);
        return rolePermissionMapper.selectByExample(rolePermissionExample);
    }
}
