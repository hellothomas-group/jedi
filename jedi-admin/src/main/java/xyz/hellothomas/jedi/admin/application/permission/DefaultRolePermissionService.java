package xyz.hellothomas.jedi.admin.application.permission;

import com.google.common.base.Preconditions;
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
import java.util.List;
import java.util.Set;
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

    @Override
    public Set<String> assignRoleToUsers(String roleName, Set<String> userIds, String operatorUserId) {
        return null;
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
//        Multimap<String, String> targetIdPermissionTypes = HashMultimap.create();
//        for (Permission permission : permissions) {
//            targetIdPermissionTypes.put(permission.getTargetId(), permission.getPermissionType());
//        }
//
//        for (String targetId : targetIdPermissionTypes.keySet()) {
//            Collection<String> permissionTypes = targetIdPermissionTypes.get(targetId);
//            List<Permission> current =
//                    permissionRepository.findByPermissionTypeInAndTargetId(permissionTypes, targetId);
//            Preconditions.checkState(CollectionUtils.isEmpty(current),
//                    "Permission with permissionType %s targetId %s already exists!", permissionTypes,
//                    targetId);
//        }
//
//        Iterable<Permission> results = permissionRepository.saveAll(permissions);
//        return StreamSupport.stream(results.spliterator(), false).collect(Collectors.toSet());
        return null;
    }
}
