package com.hellothomas.jedi.admin.domain;

import com.hellothomas.jedi.biz.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author
 */
@Getter
@Setter
public class Item extends BaseEntity {

    /**
     * executorId
     */
    private Long executorId;

    /**
     * 配置项值
     */
    private String configuration;

    /**
     * 注释
     */
    private String comment;

    @Override
    public String toString() {
        return toStringHelper().add("executorId", executorId).add("configuration", comment).add("comment", comment).toString();
    }
}