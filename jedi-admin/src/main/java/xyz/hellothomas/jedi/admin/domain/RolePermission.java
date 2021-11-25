package xyz.hellothomas.jedi.admin.domain;

import lombok.Getter;
import lombok.Setter;
import xyz.hellothomas.jedi.biz.domain.BaseEntity;

/**
 * @author
 */
@Getter
@Setter
public class RolePermission extends BaseEntity {
    /**
     * Role Id
     */
    private Long roleId;

    /**
     * Permission Id
     */
    private Long permissionId;

    @Override
    public String toString() {
        return toStringHelper().add("roleId", roleId).add("permissionId", permissionId).toString();
    }
}