package xyz.hellothomas.jedi.admin.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.hellothomas.jedi.admin.api.dto.AlarmConfigResponse;

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

    @PostMapping(value = "/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/alarm-configs")
    @ApiOperation("create")
    public AlarmConfigResponse create(@PathVariable("namespaceName") String namespaceName,
                                      @PathVariable("appId") String appId,
                                      @PathVariable("executorName") String executorName,
                                      @RequestParam("configuration") String configuration,
                                      @RequestParam("operator") String operator) {

        return null;
    }
}
