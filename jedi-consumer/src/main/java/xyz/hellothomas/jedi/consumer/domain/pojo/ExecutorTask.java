package xyz.hellothomas.jedi.consumer.domain.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 80234613 唐圆
 * @date 2021/7/13 17:21
 * @descripton
 * @version 1.0
 */
@Setter
@Getter
@ToString
public class ExecutorTask {
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
     * 任务名称
     */
    private String taskName;
}
