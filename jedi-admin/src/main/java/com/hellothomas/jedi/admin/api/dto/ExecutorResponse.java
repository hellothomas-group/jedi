package com.hellothomas.jedi.admin.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Thomas
 */
@Getter
@Setter
@ToString
public class ExecutorResponse extends BaseResponse {
    private long id;

    private String namespaceName;

    private String appId;

    private String executorName;
}
