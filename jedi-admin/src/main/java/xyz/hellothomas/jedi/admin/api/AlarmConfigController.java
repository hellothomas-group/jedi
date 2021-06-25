package xyz.hellothomas.jedi.admin.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import xyz.hellothomas.jedi.admin.api.dto.AlarmConfigResponse;
import xyz.hellothomas.jedi.admin.application.AlarmConfigService;

/**
 * @author Thomas
 * @date 2021/2/1 22:36
 * @description
 * @version 1.0
 */
@Api(value = "alarm-config", tags = "alarm-config")
@RestController
@Slf4j
public class AlarmConfigController {
    private final AlarmConfigService alarmConfigService;

    public AlarmConfigController(AlarmConfigService alarmConfigService) {
        this.alarmConfigService = alarmConfigService;
    }

    @PostMapping(value = "/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/alarm-configs")
    @ApiOperation("create")
    public AlarmConfigResponse create(@PathVariable("namespaceName") String namespaceName,
                                      @PathVariable("appId") String appId,
                                      @PathVariable("executorName") String executorName,
                                      @RequestParam("configuration") String configuration,
                                      @RequestParam("operator") String operator) {

        return alarmConfigService.save(namespaceName, appId, executorName, configuration, operator);
    }

    @ApiOperation("update")
    @PutMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/alarm-configs")
    public AlarmConfigResponse update(@PathVariable("namespaceName") String namespaceName,
                                      @PathVariable("appId") String appId,
                                      @PathVariable("executorName") String executorName,
                                      @RequestParam("configuration") String configuration,
                                      @RequestParam("operator") String operator) {
        return alarmConfigService.update(namespaceName, appId, executorName, configuration, operator);
    }

    @ApiOperation("delete")
    @DeleteMapping("/alarm-configs/{alarmConfigId}")
    public void delete(@PathVariable("alarmConfigId") long alarmConfigId, @RequestParam String operator) {
        alarmConfigService.delete(alarmConfigId, operator);
    }
}
