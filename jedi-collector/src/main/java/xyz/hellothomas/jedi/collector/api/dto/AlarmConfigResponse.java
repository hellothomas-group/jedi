package xyz.hellothomas.jedi.collector.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AlarmConfigResponse extends BaseResponse {

    private long id;

    private String namespaceName;

    private String appId;

    private String executorName;

    private String configuration;
}
