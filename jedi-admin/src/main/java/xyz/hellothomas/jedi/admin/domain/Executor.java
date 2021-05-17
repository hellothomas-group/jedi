package xyz.hellothomas.jedi.admin.domain;

import lombok.Getter;
import lombok.Setter;
import xyz.hellothomas.jedi.biz.domain.BaseEntity;


/**
 * @author Thomas
 */
@Getter
@Setter
public class Executor extends BaseEntity {

    private String namespaceName;

    private String appId;

    private String executorName;

    public Executor() {

    }

    public Executor(String namespaceName, String appId, String executorName) {
        this.namespaceName = namespaceName;
        this.appId = appId;
        this.executorName = executorName;
    }

    @Override
    public String toString() {
        return toStringHelper().add("namespaceName", namespaceName).add("appId", appId)
                .add("executorName", executorName).toString();
    }
}
