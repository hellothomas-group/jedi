package xyz.hellothomas.jedi.biz.domain.monitor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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

    @Getter
    @Setter
    @ToString
    public static class ConfigurationProperty {
        private boolean alarmEnabled;

        private int queueThreshold;

        /**
         * 0-100
         */
        private int poolActivationThreshold;

        private int rejectCountThreshold;
    }
}