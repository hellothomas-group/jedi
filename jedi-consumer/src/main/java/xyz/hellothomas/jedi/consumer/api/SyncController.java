package xyz.hellothomas.jedi.consumer.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import xyz.hellothomas.jedi.biz.common.enums.SyncOperationEnum;
import xyz.hellothomas.jedi.biz.common.enums.SyncTypeEnum;
import xyz.hellothomas.jedi.biz.domain.monitor.App;
import xyz.hellothomas.jedi.biz.domain.monitor.User;
import xyz.hellothomas.jedi.consumer.application.AppService;
import xyz.hellothomas.jedi.consumer.application.UserService;
import xyz.hellothomas.jedi.core.dto.ApiResponse;
import xyz.hellothomas.jedi.core.utils.JsonUtil;

/**
 * @author Thomas
 * @date 2021/11/9 22:34
 * @description
 * @version 1.0
 */
@Slf4j
@Api(value = "sync", tags = "sync")
@RequestMapping(value = "/sync")
@RestController
public class SyncController {
    private final UserService userService;
    private final AppService appService;

    public SyncController(UserService userService, AppService appService) {
        this.userService = userService;
        this.appService = appService;
    }

    @PostMapping(value = "/syncType/{syncType}/syncOperation/{syncOperation}")
    @ApiOperation("handle")
    public ApiResponse<String> handle(@PathVariable("syncType") SyncTypeEnum syncType,
                                      @PathVariable("syncOperation") SyncOperationEnum syncOperation,
                                      @RequestBody String source) {
        log.info("syncType:{}, syncOperation:{}, source:{}", syncType, syncOperation, source);
        if (SyncTypeEnum.APP == syncType) {
            App app = JsonUtil.deserialize(source, App.class);
            appService.save(app);
        } else if (SyncTypeEnum.USER == syncType) {
            User user = JsonUtil.deserialize(source, User.class);
            userService.saveUser(user);
        } else {
            return ApiResponse.fail("不支持该同步类型");
        }
        return ApiResponse.success("同步成功");
    }
}
