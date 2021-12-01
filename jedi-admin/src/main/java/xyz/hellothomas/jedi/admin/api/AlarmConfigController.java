package xyz.hellothomas.jedi.admin.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import xyz.hellothomas.jedi.admin.api.dto.AlarmConfigResponse;
import xyz.hellothomas.jedi.admin.application.AlarmConfigService;
import xyz.hellothomas.jedi.admin.infrastructure.annotation.PreAuthorize;
import xyz.hellothomas.jedi.admin.infrastructure.annotation.UserLoginToken;
import xyz.hellothomas.jedi.core.dto.ApiResponse;

import static xyz.hellothomas.jedi.admin.common.utils.JwtUtil.CLAIM_USER_NAME;

/**
 * @author Thomas
 * @date 2021/2/1 22:36
 * @description
 * @version 1.0
 */
@UserLoginToken
@Api(value = "alarm-config", tags = "alarm-config")
@RestController
public class AlarmConfigController {
    private final AlarmConfigService alarmConfigService;

    public AlarmConfigController(AlarmConfigService alarmConfigService) {
        this.alarmConfigService = alarmConfigService;
    }

    @PreAuthorize(value = "@permissionValidator.hasModifyExecutorAlarmConfigPermission(#namespaceName, #appId, " +
            "#executorName, #operator)")
    @PostMapping(value = "/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/alarm-configs")
    @ApiOperation("create")
    public ApiResponse<AlarmConfigResponse> create(@PathVariable("namespaceName") String namespaceName,
                                                   @PathVariable("appId") String appId,
                                                   @PathVariable("executorName") String executorName,
                                                   @RequestParam("configuration") String configuration,
                                                   @RequestAttribute(CLAIM_USER_NAME) String operator) {

        return ApiResponse.success(alarmConfigService.save(namespaceName, appId, executorName, configuration,
                operator));
    }

    @PreAuthorize(value = "@permissionValidator.hasModifyExecutorAlarmConfigPermission(#namespaceName, #appId, " +
            "#executorName, #operator)")
    @ApiOperation("update")
    @PutMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/alarm-configs")
    public ApiResponse<AlarmConfigResponse> update(@PathVariable("namespaceName") String namespaceName,
                                                   @PathVariable("appId") String appId,
                                                   @PathVariable("executorName") String executorName,
                                                   @RequestParam("configuration") String configuration,
                                                   @RequestAttribute(CLAIM_USER_NAME) String operator) {
        return ApiResponse.success(alarmConfigService.update(namespaceName, appId, executorName, configuration,
                operator));
    }

    @ApiOperation("delete")
    @DeleteMapping("/alarm-configs/{alarmConfigId}")
    public ApiResponse<String> delete(@PathVariable("alarmConfigId") long alarmConfigId,
                                      @RequestAttribute(CLAIM_USER_NAME) String operator) {
        alarmConfigService.delete(alarmConfigId, operator);
        return ApiResponse.success("删除成功");
    }

    @Deprecated
    @GetMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/alarm-configs")
    public AlarmConfigResponse get(@PathVariable("namespaceName") String namespaceName,
                                   @PathVariable("appId") String appId,
                                   @PathVariable("executorName") String executorName) {
        return alarmConfigService.findOne(namespaceName, appId, executorName);
    }
}
