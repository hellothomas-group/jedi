package xyz.hellothomas.jedi.consumer.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlarmConfig extends BaseEntity {
    /**
     * executorId
     */
    private Long executorId;

    /**
     * 配置项值
     */
    private String configuration;

    @Override
    public String toString() {
        return toStringHelper().add("executorId", executorId)
                .add("configuration", configuration).toString();
    }
}
