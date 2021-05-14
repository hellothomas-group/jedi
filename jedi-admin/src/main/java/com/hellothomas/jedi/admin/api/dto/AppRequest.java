package com.hellothomas.jedi.admin.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @author 80234613
 */
@Getter
@Setter
@ToString
public class AppRequest {

    @NotBlank
    private String namespaceName;

    @NotBlank
    private String appId;

    private String appDescription;
}
