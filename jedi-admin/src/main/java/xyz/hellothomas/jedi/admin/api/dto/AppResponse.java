package xyz.hellothomas.jedi.admin.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AppResponse extends BaseResponse {

    private long id;

    private String namespaceName;

    private String appId;

    private String appDescription;
}
