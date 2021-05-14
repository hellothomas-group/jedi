package com.hellothomas.jedi.admin.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class InstanceConfigResponse {
    private ReleaseResponse release;

    private LocalDateTime releaseDeliveryTime;

    private LocalDateTime dataChangeLastModifiedTime;
}
