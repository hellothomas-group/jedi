package xyz.hellothomas.jedi.admin.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 80234613
 */
@Getter
@Setter
@ToString
public class InstanceResponse {
    private Long id;

    private String namespaceName;

    private String appId;

    private String ip;

    private List<InstanceConfigResponse> configs;

    private LocalDateTime dataChangeCreatedTime;
}
