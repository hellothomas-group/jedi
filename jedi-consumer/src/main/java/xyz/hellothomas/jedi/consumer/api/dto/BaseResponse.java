package xyz.hellothomas.jedi.consumer.api.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author Thomas
 */
@Getter
@Setter
@ToString
public class BaseResponse {

    protected String dataChangeCreatedBy;

    protected String dataChangeLastModifiedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime dataChangeCreatedTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime dataChangeLastModifiedTime;
}
