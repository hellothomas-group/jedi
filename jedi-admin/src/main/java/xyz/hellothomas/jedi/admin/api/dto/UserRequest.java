package xyz.hellothomas.jedi.admin.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @author Thomas
 * @date 2021/6/27 22:13
 * @description
 * @version 1.0
 */
@Getter
@Setter
@ToString
public class UserRequest {
    /**
     * 用户名
     */
    @NotBlank
    private String userName;

    /**
     * 用户密码
     */
    @NotBlank
    private String password;

    /**
     * 真实名称
     */
    private String realName;

    /**
     * 邮箱
     */
    private String email;
}
