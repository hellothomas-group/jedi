package xyz.hellothomas.jedi.admin.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Thomas
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataChangeCreatedTime;
}
