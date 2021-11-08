package xyz.hellothomas.jedi.admin.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @author Thomas
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
