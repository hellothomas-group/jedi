package xyz.hellothomas.jedi.admin.domain;

import lombok.Getter;
import lombok.Setter;
import xyz.hellothomas.jedi.biz.domain.BaseEntity;

@Getter
@Setter
public class AlarmConfig extends BaseEntity {

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
     * 配置项值
     */
    private String configuration;

    @Override
    public String toString() {
        return toStringHelper().add("namespaceName", namespaceName)
                .add("appId", appId).add("executorName", executorName)
                .add("configuration", configuration).toString();
    }
}