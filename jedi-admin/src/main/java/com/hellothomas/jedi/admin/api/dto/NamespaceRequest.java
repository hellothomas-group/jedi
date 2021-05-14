package com.hellothomas.jedi.admin.api.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class NamespaceRequest {

    @NotBlank
    private String name;

    private String description;

    private String orgId;

    private String orgName;

    private String ownerName;

    private String ownerEmail;
}
