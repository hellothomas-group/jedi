package xyz.hellothomas.jedi.biz.domain.config;

import lombok.Getter;
import lombok.Setter;
import xyz.hellothomas.jedi.biz.domain.BaseEntity;

@Getter
@Setter
public class Release extends BaseEntity {
    /**
     * 发布的Key
     */
    private String releaseKey;

    /**
     * 发布名字
     */
    private String name;

    private String namespaceName;

    private String appId;

    private String executorName;

    /**
     * 发布配置
     */
    private String configurations;

    /**
     * 发布说明
     */
    private String comment;

    /**
     * 是否废弃
     */
    private Boolean isAbandoned;

    @Override
    public String toString() {
        return toStringHelper().add("name", name)
                .add("namespaceName", namespaceName).add("appId", appId)
                .add("executorName", executorName).add("configurations", configurations)
                .add("comment", comment).add("isAbandoned", isAbandoned).toString();
    }
}
