package xyz.hellothomas.jedi.admin.api.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class ReleaseHistoryResponse extends BaseResponse {

    private Long id;

    private String namespaceName;

    private String appId;

    private String executorName;

    private long releaseId;

    private long previousReleaseId;

    private int operation;

    private Map<String, Object> operationContext;
}
