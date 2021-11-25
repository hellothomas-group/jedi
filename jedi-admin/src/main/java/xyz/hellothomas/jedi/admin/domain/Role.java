package xyz.hellothomas.jedi.admin.domain;

import lombok.Getter;
import lombok.Setter;
import xyz.hellothomas.jedi.biz.domain.BaseEntity;

/**
 * @author
 */
@Getter
@Setter
public class Role extends BaseEntity {
    /**
     * roleName
     */
    private String roleName;

    @Override
    public String toString() {
        return toStringHelper().add("roleName", roleName).toString();
    }
}