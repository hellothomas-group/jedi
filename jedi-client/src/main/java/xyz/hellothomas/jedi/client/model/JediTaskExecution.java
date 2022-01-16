package xyz.hellothomas.jedi.client.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

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
     * taskExtraData
     */
    private String taskExtraData;

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
    private int status;

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
    private boolean isRecoverable;

    /**
     * recovered
     */
    private boolean isRecovered;

    /**
     * host
     */
    private String host;

    /**
     * machineId
     */
    private String machineId;

    /**
     * traceId
     */
    private String traceId;

    /**
     * byRetryer
     */
    private boolean isByRetryer;

    /**
     * previousId
     */
    private String previousId;

    /**
     * parentId
     */
    private String parentId;

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
