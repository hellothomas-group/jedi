package xyz.hellothomas.jedi.admin.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Thomas
 */
@Getter
@Setter
@ToString
public class ReleaseResponse extends BaseResponse {
    private Long id;

    private String releaseKey;

    private String name;

    private String namespaceName;

    private String appId;

    private String executorName;

    private String configurations;

    private String comment;

    private Boolean isAbandoned;
}
