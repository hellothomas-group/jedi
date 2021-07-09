package xyz.hellothomas.jedi.admin.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.hellothomas.jedi.core.dto.ApiResponse;
import xyz.hellothomas.jedi.admin.api.dto.UserRequest;
import xyz.hellothomas.jedi.admin.application.UserService;
import xyz.hellothomas.jedi.admin.domain.User;
import xyz.hellothomas.jedi.biz.common.utils.LocalBeanUtils;

/**
 * @author Thomas
 * @date 2021/6/27 22:18
 * @description
 * @version 1.0
 */
@Api(value = "user", tags = "user")
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/create")
    @ApiOperation("create")
    public ApiResponse<Integer> create(@RequestBody UserRequest userRequest) {
        User user = LocalBeanUtils.transform(User.class, userRequest);
        user.setIsManual(true);
        userService.saveUser(user);
        return ApiResponse.success(user.getId());
    }
}
