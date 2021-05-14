package com.hellothomas.jedi.admin.api.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NamespaceResponse extends BaseResponse {

    private long id;

    private String name;

    private String description;
    
    private String orgId;

    private String orgName;

    private String ownerName;

    private String ownerEmail;
}
