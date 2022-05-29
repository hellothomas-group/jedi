package xyz.hellothomas.jedi.collector.domain;

import lombok.Getter;
import lombok.Setter;
import xyz.hellothomas.jedi.biz.domain.BaseEntity;

@Getter
@Setter
public class ExecutorTask extends BaseEntity {
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
     * taskName
     */
    private String taskName;

    @Override
    public String toString() {
        return toStringHelper().add("namespaceName", namespaceName).add("appId", appId)
                .add("executorName", executorName).add("taskName", taskName).toString();
    }
}