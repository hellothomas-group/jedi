package xyz.hellothomas.jedi.admin.domain;

import lombok.Getter;
import lombok.Setter;
import xyz.hellothomas.jedi.biz.domain.BaseEntity;

/**
 * @author
 */
@Getter
@Setter
public class Permission extends BaseEntity {

    /**
     * 权限类型
     */
    private String permissionType;

    /**
     * 权限对象类型
     */
    private String targetId;

    @Override
    public String toString() {
        return toStringHelper().add("permissionType", permissionType)
                .add("targetId", targetId).toString();
    }
}