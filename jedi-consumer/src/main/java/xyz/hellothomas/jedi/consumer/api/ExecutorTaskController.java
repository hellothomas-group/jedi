package xyz.hellothomas.jedi.consumer.api;

import io.swagger.annotations.Api;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.hellothomas.jedi.consumer.api.dto.ExecutorTaskDetailResponse;
import xyz.hellothomas.jedi.consumer.api.dto.PageHelperRequest;
import xyz.hellothomas.jedi.consumer.api.dto.PageResult;
import xyz.hellothomas.jedi.consumer.application.ExecutorTaskService;
import xyz.hellothomas.jedi.consumer.common.util.LocalBeanUtils;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskMessage;
import xyz.hellothomas.jedi.core.dto.ApiResponse;

import java.time.LocalDateTime;
import java.util.List;

@Api(value = "executor-task", tags = "executor-task")
@RestController
public class ExecutorTaskController {

    private final ExecutorTaskService executorTaskService;

    public ExecutorTaskController(ExecutorTaskService executorTaskService) {
        this.executorTaskService = executorTaskService;
    }

    @GetMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/task-details")
    public ApiResponse<PageResult<ExecutorTaskDetailResponse>> find(@PathVariable("namespaceName") String namespaceName,
                                                                    @PathVariable("appId") String appId,
                                                                    @PathVariable("executorName") String executorName,
                                                                    @RequestParam("taskName") String taskName,
                                                                    @RequestParam(value = "instanceIp", required =
                                                                            false) String instanceIp,
                                                                    @RequestParam("startTime") @DateTimeFormat(pattern = "yyyy-MM-dd " +
                                                                            "HH:mm:ss") LocalDateTime startTime,
                                                                    @RequestParam("endTime") @DateTimeFormat(pattern
                                                                            = "yyyy-MM-dd " +
                                                                            "HH:mm:ss") LocalDateTime endTime,
                                                                    PageHelperRequest pageHelperRequest) {
        PageResult<ExecutorTaskMessage> taskMessagePageResult =
                executorTaskService.findByTaskNameAndRecordTime(namespaceName
                        , appId, executorName, taskName, instanceIp, startTime, endTime, pageHelperRequest);
        PageResult<ExecutorTaskDetailResponse> executorStatusResponsePageResult =
                transform2PageResult(taskMessagePageResult);

        return ApiResponse.success(executorStatusResponsePageResult);
    }

    private PageResult<ExecutorTaskDetailResponse> transform2PageResult(PageResult<ExecutorTaskMessage> taskMessagePageResult) {
        List<ExecutorTaskDetailResponse> executorTaskDetailRespons =
                LocalBeanUtils.batchTransform(ExecutorTaskDetailResponse.class,
                taskMessagePageResult.getContent());

        return new PageResult<>(executorTaskDetailRespons, taskMessagePageResult.getTotal(),
                taskMessagePageResult.getPageNum(),
                taskMessagePageResult.getPageSize());
    }
}
