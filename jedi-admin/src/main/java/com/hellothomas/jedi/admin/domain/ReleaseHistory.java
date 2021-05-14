package com.hellothomas.jedi.admin.domain;

import com.hellothomas.jedi.biz.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReleaseHistory extends BaseEntity {

    /**
     * namespaceName
     */
    private String namespaceName;

    /**
     * appId
     */
    private String appId;

    /**
     * executorName
     */
    private String executorName;

    /**
     * 关联的releaseId
     */
    private Long releaseId;

    /**
     * 前一次发布的releaseId
     */
    private Long previousReleaseId;

    /**
     * 发布类型，0: 普通发布，1: 回滚
     */
    private Integer operation;

    /**
     * 发布上下文信息
     */
    private String operationContext;

    @Override
    public String toString() {
        return toStringHelper().add("namespaceName", namespaceName).add("appId", appId)
                .add("executorName", executorName).add("releaseId", releaseId)
                .add("previousReleaseId", previousReleaseId).add("operation", operation)
                .add("operationContext", operationContext)
                .toString();
    }
}
