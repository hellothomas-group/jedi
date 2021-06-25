package xyz.hellothomas.jedi.consumer.domain;

import lombok.Getter;
import lombok.Setter;

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
