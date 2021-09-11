package xyz.hellothomas.jedi.admin.application;

import org.springframework.stereotype.Service;
import xyz.hellothomas.jedi.admin.api.dto.LoginRequest;
import xyz.hellothomas.jedi.admin.api.dto.LoginResponse;
import xyz.hellothomas.jedi.admin.common.enums.AdminErrorCodeEnum;
import xyz.hellothomas.jedi.admin.common.utils.JwtUtil;
import xyz.hellothomas.jedi.admin.domain.User;
import xyz.hellothomas.jedi.biz.common.utils.LocalBeanUtils;
import xyz.hellothomas.jedi.core.exception.BusinessException;

/**
 * @author Thomas
 * @date 2021/6/27 22:24
 * @description
 * @version 1.0
 */
@Service
public class LoginService {
    private final UserService userService;

    public LoginService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 登录验证
     *
     * @param loginRequest
     * @return LoginResponse
     */
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userService.getUserByUserName(loginRequest.getUsername());
        if (user == null) {
            throw new BusinessException(AdminErrorCodeEnum.USER_NOT_EXIST);
        }

        if (!loginRequest.getPassword().equals(user.getPassword())) {
            throw new BusinessException(AdminErrorCodeEnum.PASSWORD_INVALID);
        }

        LoginResponse loginResponse = LocalBeanUtils.transform(LoginResponse.class, user);
        loginResponse.setToken(JwtUtil.createToken(user));

        return loginResponse;
    }

    /**
     * 登录验证
     *
     * @param code
     * @return token
     */
    public LoginResponse loginForExternalAuth(String code) {
        // 根据code获取用户信息

        // 自动生成对应的用户信息
        User user = userService.getUserByUserName("");
        if (user == null) {
            user = new User();
            userService.saveUser(user);
        }

        // 生成token
        LoginResponse loginResponse = LocalBeanUtils.transform(LoginResponse.class, user);
        loginResponse.setToken(JwtUtil.createToken(user));

        return loginResponse;
    }
}
