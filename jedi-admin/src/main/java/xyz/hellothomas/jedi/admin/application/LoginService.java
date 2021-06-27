package xyz.hellothomas.jedi.admin.application;

import org.springframework.stereotype.Service;

/**
 * @author Thomas
 * @date 2021/6/27 22:24
 * @description
 * @version 1.0
 */
@Service
public class LoginService {
    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid) {
        return null;
    }
}
