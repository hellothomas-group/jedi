package xyz.hellothomas.jedi.collector.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import xyz.hellothomas.jedi.biz.common.utils.LocalBeanUtils;
import xyz.hellothomas.jedi.biz.domain.monitor.AlarmConfig;
import xyz.hellothomas.jedi.biz.infrastructure.exception.BadRequestException;
import xyz.hellothomas.jedi.biz.infrastructure.exception.NotFoundException;
import xyz.hellothomas.jedi.collector.api.dto.AlarmConfigResponse;
import xyz.hellothomas.jedi.collector.application.AlarmConfigService;
import xyz.hellothomas.jedi.core.dto.ApiResponse;
import xyz.hellothomas.jedi.core.utils.JsonUtil;

import java.time.LocalDateTime;

/**
 * @author Thomas
 * @date 2021/2/1 22:36
 * @description
 * @version 1.0
 */
@Api(value = "alarm-config", tags = "alarm-config")
@RestController
public class AlarmConfigController {
    private final AlarmConfigService alarmConfigService;

    public AlarmConfigController(AlarmConfigService alarmConfigService) {
        this.alarmConfigService = alarmConfigService;
    }

    @PostMapping(value = "/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/alarm-configs")
    @ApiOperation("create")
    public ApiResponse<AlarmConfigResponse> create(@PathVariable("namespaceName") String namespaceName,
                                                   @PathVariable("appId") String appId,
                                                   @PathVariable("executorName") String executorName,
                                                   @RequestParam("configuration") String configuration,
                                                   @RequestParam("operator") String operator) {

        AlarmConfig managedEntity = alarmConfigService.findOne(namespaceName, appId, executorName);
        if (managedEntity != null) {
            throw new BadRequestException("alarmConfig already exists");
        }
        AlarmConfig entity = alarmConfigService.save(namespaceName, appId, executorName, configuration, operator);
        AlarmConfigResponse alarmConfigResponse = LocalBeanUtils.transform(AlarmConfigResponse.class, entity);
        return ApiResponse.success(alarmConfigResponse);
    }

    @ApiOperation("update")
    @PutMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/alarm-configs")
    public ApiResponse<AlarmConfigResponse> update(@PathVariable("namespaceName") String namespaceName,
                                                   @PathVariable("appId") String appId,
                                                   @PathVariable("executorName") String executorName,
                                                   @RequestParam("configuration") String configuration,
                                                   @RequestParam("operator") String operator) {
        if (!JsonUtil.isJSONValid(configuration)) {
            throw new BadRequestException("configuration invalid, must be json");
        }

        AlarmConfig managedEntity = alarmConfigService.findOne(namespaceName, appId, executorName);
        if (managedEntity == null || managedEntity.getIsDeleted()) {
            throw new BadRequestException("alarmConfig not exist");
        }

        managedEntity.setConfiguration(configuration);
        managedEntity.setUpdateUser(operator);
        managedEntity.setUpdateTime(LocalDateTime.now());
        alarmConfigService.update(managedEntity);

        AlarmConfigResponse alarmConfigResponse = LocalBeanUtils.transform(AlarmConfigResponse.class, managedEntity);
        return ApiResponse.success(alarmConfigResponse);
    }

    @ApiOperation("delete")
    @DeleteMapping("/alarm-configs/{alarmConfigId}")
    public ApiResponse<String> delete(@PathVariable("alarmConfigId") long alarmConfigId,
                                      @RequestParam String operator) {
        AlarmConfig entity = alarmConfigService.findOne(alarmConfigId);
        if (entity == null) {
            throw new NotFoundException("alarmConfig not found for alarmConfigId " + alarmConfigId);
        }
        alarmConfigService.delete(entity.getId(), operator);
        return ApiResponse.success("删除成功");
    }

    @GetMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/alarm-configs")
    public ApiResponse<AlarmConfigResponse> get(@PathVariable("namespaceName") String namespaceName,
                                                @PathVariable("appId") String appId,
                                                @PathVariable("executorName") String executorName) {
        AlarmConfig alarmConfig = alarmConfigService.findOne(namespaceName, appId, executorName);
        if (alarmConfig == null) {
            throw new NotFoundException(
                    String.format("alarmConfig not found for %s %s %s", namespaceName, appId, executorName));
        }
        return ApiResponse.success(LocalBeanUtils.transform(AlarmConfigResponse.class, alarmConfig));
    }
}
