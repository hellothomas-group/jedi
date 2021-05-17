package xyz.hellothomas.jedi.admin.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ItemResponse extends BaseResponse {

    private Long id;

    private Long executorId;

    private String configuration;

    private String comment;
}
