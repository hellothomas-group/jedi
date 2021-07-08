package xyz.hellothomas.jedi.admin.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Thomas
 * @date 2021/6/27 22:13
 * @description
 * @version 1.0
 */
@Getter
@Setter
@ToString
public class LoginResponse {
    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户真实名
     */
    private String realName;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * token
     */
    private String token;
}
