package xyz.hellothomas.jedi.client.persistence;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TaskPersistProperty {
    private String dataSourceName;
    private String id;
    private String namespaceName;
    private String appId;
    private String executorName;
    private String taskName;
    private String processStatus;
    private String beanName;
    private String methodName;
    private String arguments;
    private String traceId;
    private String lastId;
}
