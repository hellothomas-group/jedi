package xyz.hellothomas.jedi.consumer.api.dto;


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

    protected LocalDateTime dataChangeCreatedTime;

    protected LocalDateTime dataChangeLastModifiedTime;
}
