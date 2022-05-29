package xyz.hellothomas.jedi.collector.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author Thomas
 */
@Getter
@Setter
@ToString
@Valid
@ApiModel(description = "分页请求对象")
public class PageHelperRequest {
    @ApiModelProperty(value = "页面大小", required = true)
    @NotNull
    private Integer pageSize;

    @ApiModelProperty(value = "页码", required = true)
    @NotNull
    private Integer pageNum;
}
