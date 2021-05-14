package com.hellothomas.jedi.admin.domain;

import com.hellothomas.jedi.biz.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author
 */
@Getter
@Setter
@ToString
public class Namespace extends BaseEntity {

    /**
     * namespace名字，注意，需要全局唯一
     */
    private String name;

    /**
     * namespace描述
     */
    private String description;

    /**
     * 部门Id
     */
    private String orgId;

    /**
     * 部门名字
     */
    private String orgName;

    /**
     * ownerName
     */
    private String ownerName;

    /**
     * ownerEmail
     */
    private String ownerEmail;
}