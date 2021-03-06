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
public class LoginRequest {
    /**
     * 用户名
     */
    @NotBlank
    private String username;

    /**
     * 用户密码
     */
    @NotBlank
    private String password;

    /**
     * 验证码
     */
    private String code;
}
