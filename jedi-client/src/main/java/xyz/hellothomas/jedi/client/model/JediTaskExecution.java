package xyz.hellothomas.jedi.client.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
public class JediTaskExecution {
    /**
     * id
     */
    private String id;

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

    /**
     * createTime
     */
    private LocalDateTime createTime;

    /**
     * startTime
     */
    private LocalDateTime startTime;

    /**
     * endTime
     */
    private LocalDateTime endTime;

    /**
     * 0: created, 1: success, 2: fail
     */
    private String status;

    /**
     * exitCode
     */
    private String exitCode;

    /**
     * exitMessage
     */
    private String exitMessage;

    /**
     * beanName
     */
    private String beanName;

    /**
     * beanTypeName
     */
    private String beanTypeName;

    /**
     * methodName
     */
    private String methodName;

    /**
     * methodParamTypes
     */
    private String methodParamTypes;

    /**
     * methodArguments
     */
    private String methodArguments;

    /**
     * recoverable
     */
    private boolean recoverable;

    /**
     * host
     */
    private String host;

    /**
     * traceId
     */
    private String traceId;

    /**
     * previousId
     */
    private String previousId;

    /**
     * dataSourceName
     */
    private String dataSourceName;

    /**
     * lastUpdatedUser
     */
    private String lastUpdatedUser;

    /**
     * lastUpdatedTime
     */
    private LocalDateTime lastUpdatedTime;
}
