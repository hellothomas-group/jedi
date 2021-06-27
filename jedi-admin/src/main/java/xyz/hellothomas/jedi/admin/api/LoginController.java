package xyz.hellothomas.jedi.admin.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.hellothomas.jedi.admin.api.dto.ApiResponse;
import xyz.hellothomas.jedi.admin.api.dto.LoginRequest;
import xyz.hellothomas.jedi.admin.application.LoginService;

/**
 * @author Thomas
 * @date 2021/6/27 22:18
 * @description
 * @version 1.0
 */
@Api(value = "login", tags = "login")
@RestController
@Slf4j
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(value = "/login")
    @ApiOperation("login")
    public ApiResponse<String> create(@RequestBody LoginRequest loginRequest) {

        return null;
    }
}
