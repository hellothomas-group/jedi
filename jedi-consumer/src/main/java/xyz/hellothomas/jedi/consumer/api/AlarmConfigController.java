package xyz.hellothomas.jedi.consumer.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.hellothomas.jedi.consumer.api.dto.AlarmConfigResponse;
import xyz.hellothomas.jedi.consumer.application.AlarmConfigService;
import xyz.hellothomas.jedi.consumer.common.util.LocalBeanUtils;
import xyz.hellothomas.jedi.consumer.domain.AlarmConfig;
import xyz.hellothomas.jedi.consumer.infrastructure.exception.BadRequestException;

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

        AlarmConfig managedEntity = alarmConfigService.findOne(namespaceName, appId, executorName);
        if (managedEntity != null) {
            throw new BadRequestException("alarmConfig already exists");
        }
        AlarmConfig entity = alarmConfigService.save(namespaceName, appId, executorName, configuration, operator);
        return LocalBeanUtils.transform(AlarmConfigResponse.class, entity);
    }
}
