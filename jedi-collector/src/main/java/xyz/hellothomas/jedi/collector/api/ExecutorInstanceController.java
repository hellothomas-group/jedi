package xyz.hellothomas.jedi.collector.api;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import xyz.hellothomas.jedi.biz.common.utils.LocalBeanUtils;
import xyz.hellothomas.jedi.collector.api.dto.ExecutorInstanceResponse;
import xyz.hellothomas.jedi.collector.api.dto.PageHelperRequest;
import xyz.hellothomas.jedi.collector.api.dto.PageResult;
import xyz.hellothomas.jedi.collector.application.ExecutorInstanceService;
import xyz.hellothomas.jedi.collector.domain.ExecutorInstance;
import xyz.hellothomas.jedi.core.dto.ApiResponse;

import java.util.List;

@Api(value = "executor-instance", tags = "executor-instance")
@RestController
public class ExecutorInstanceController {
    private final ExecutorInstanceService executorInstanceService;

    public ExecutorInstanceController(ExecutorInstanceService executorInstanceService) {
        this.executorInstanceService = executorInstanceService;
    }

    @GetMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/instances")
    public ApiResponse<PageResult<ExecutorInstanceResponse>> getByRelease(@PathVariable("namespaceName") String namespaceName,
                                                                          @PathVariable("appId") String appId,
                                                                          @PathVariable("executorName") String executorName,
                                                                          PageHelperRequest pageHelperRequest) {
        PageResult<ExecutorInstance> ExecutorInstancePageResult =
                executorInstanceService.findInstances(namespaceName, appId, executorName, pageHelperRequest);

        PageResult<ExecutorInstanceResponse> responsePageResult =
                transformInstance2PageResult(ExecutorInstancePageResult);
        return ApiResponse.success(responsePageResult);
    }

    private PageResult<ExecutorInstanceResponse> transformInstance2PageResult(
            PageResult<ExecutorInstance> executorInstancePageResult) {
        List<ExecutorInstanceResponse> InstanceResponses =
                LocalBeanUtils.batchTransform(ExecutorInstanceResponse.class, executorInstancePageResult.getContent());

        return new PageResult<>(InstanceResponses, executorInstancePageResult.getTotal(),
                executorInstancePageResult.getPageNum(),
                executorInstancePageResult.getPageSize());
    }
}
