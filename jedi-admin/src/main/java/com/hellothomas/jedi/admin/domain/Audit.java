package com.hellothomas.jedi.admin.domain;

import com.hellothomas.jedi.biz.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Thomas
 */
@Getter
@Setter
public class Audit extends BaseEntity {

    public enum OP {
        INSERT, UPDATE, DELETE
    }

    /**
     * 表名
     */
    private String entityName;

    /**
     * 记录ID
     */
    private Long entityId;

    /**
     * 操作类型
     */
    private String operationName;

    /**
     * 备注
     */
    private String comment;

    @Override
    public String toString() {
        return toStringHelper().add("entityName", entityName).add("entityId", entityId)
                .add("operationName", operationName).add("comment", comment).toString();
    }
}
