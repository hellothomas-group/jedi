package xyz.hellothomas.jedi.admin.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import xyz.hellothomas.jedi.admin.infrastructure.annotation.UserLoginToken;
import xyz.hellothomas.jedi.core.dto.ApiResponse;

import static xyz.hellothomas.jedi.admin.common.utils.JwtUtil.CLAIM_USER_NAME;

/**
 * @author Thomas
 * @date 2021/2/1 22:36
 * @description
 * @version 1.0
 */
@Slf4j
@UserLoginToken
@Api(value = "executor-task", tags = "executor-task")
@RestController
public class ExecutorTaskController {
    private final RestTemplate restTemplate;

    public ExecutorTaskController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    //    @PreAuthorize(value = "@permissionValidator.hasReleaseExecutorConfigPermission(#namespaceName, #appId, " +
//            "#executorName, #operator)")
    @PostMapping(value = "/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/tasks/{taskId}/retry")
    @ApiOperation("retry")
    public ApiResponse<String> retry(@PathVariable("namespaceName") String namespaceName,
                                     @PathVariable("appId") String appId,
                                     @PathVariable("executorName") String executorName,
                                     @PathVariable("taskId") String taskId,
                                     @RequestParam("url") String url,
                                     @RequestParam(value = "dataSourceName", required = false) String dataSourceName,
                                     @RequestAttribute(CLAIM_USER_NAME) String operator) {
        log.info("namespace:{}, appId:{}, executorName:{}, taskId:{}, dataSourceName:{}", namespaceName, appId,
                executorName, taskId, dataSourceName);

        restTemplate.exchange(url + "?taskId={taskId}&&dataSourceName={dataSourceName}&&operator={operator}",
                HttpMethod.POST, null,
                Void.class, taskId, dataSourceName, operator);
        return ApiResponse.success("重试提交成功");
    }
}
