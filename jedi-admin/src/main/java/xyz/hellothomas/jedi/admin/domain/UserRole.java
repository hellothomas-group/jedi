package xyz.hellothomas.jedi.admin.domain;

import lombok.Getter;
import lombok.Setter;
import xyz.hellothomas.jedi.biz.domain.BaseEntity;

/**
 * @author
 */
@Getter
@Setter
public class UserRole extends BaseEntity {
    /**
     * 用户身份标识
     */
    private String userId;

    /**
     * roleId
     */
    private Long roleId;

    @Override
    public String toString() {
        return toStringHelper().add("userId", userId).add("roleId", roleId).toString();
    }
}