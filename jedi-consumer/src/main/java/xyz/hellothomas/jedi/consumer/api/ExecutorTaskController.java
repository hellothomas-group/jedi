package xyz.hellothomas.jedi.consumer.api;

import io.swagger.annotations.Api;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.hellothomas.jedi.consumer.api.dto.*;
import xyz.hellothomas.jedi.consumer.application.ExecutorTaskService;
import xyz.hellothomas.jedi.consumer.application.ExecutorTaskStatisticsService;
import xyz.hellothomas.jedi.consumer.common.util.LocalBeanUtils;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskMessage;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskStatistics;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskStatisticsHistory;
import xyz.hellothomas.jedi.core.dto.ApiResponse;

import java.time.LocalDateTime;
import java.util.List;

@Api(value = "executor-task", tags = "executor-task")
@RestController
public class ExecutorTaskController {

    private final ExecutorTaskService executorTaskService;
    private final ExecutorTaskStatisticsService executorTaskStatisticsService;

    public ExecutorTaskController(ExecutorTaskService executorTaskService,
                                  ExecutorTaskStatisticsService executorTaskStatisticsService) {
        this.executorTaskService = executorTaskService;
        this.executorTaskStatisticsService = executorTaskStatisticsService;
    }

    @GetMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/task-details")
    public ApiResponse<PageResult<ExecutorTaskDetailResponse>> findDetail(@PathVariable("namespaceName") String namespaceName,
                                                                          @PathVariable("appId") String appId,
                                                                          @PathVariable("executorName") String executorName,
                                                                          @RequestParam(value = "taskName",
                                                                                  defaultValue = "DEFAULT") String taskName,
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

    @GetMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/task-statistics")
    public ApiResponse<ExecutorTaskStatisticsResponse> findStatistics(@PathVariable("namespaceName") String namespaceName,
                                                                      @PathVariable("appId") String appId,
                                                                      @PathVariable("executorName") String executorName,
                                                                      @RequestParam(value = "taskName", defaultValue =
                                                                              "DEFAULT") String taskName,
                                                                      PageHelperRequest pageHelperRequest) {
        ExecutorTaskStatistics executorTaskStatistics =
                executorTaskStatisticsService.findCurrentOne(namespaceName, appId, executorName, taskName);

        return ApiResponse.success(LocalBeanUtils.transform(ExecutorTaskStatisticsResponse.class,
                executorTaskStatistics));
    }

    @GetMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/task-statistics-history")
    public ApiResponse<PageResult<ExecutorTaskStatisticsHistoryResponse>> findStatisticsHistory(
            @PathVariable("namespaceName") String namespaceName,
            @PathVariable("appId") String appId,
            @PathVariable("executorName") String executorName,
            @RequestParam(value = "taskName", defaultValue =
                    "DEFAULT") String taskName,
            PageHelperRequest pageHelperRequest) {
        PageResult<ExecutorTaskStatisticsHistory> executorTaskStatisticsHistoryPageResult =
                executorTaskStatisticsService.findHistory(namespaceName, appId, executorName, taskName,
                        pageHelperRequest);
        PageResult<ExecutorTaskStatisticsHistoryResponse> responsePageResult =
                transformHistory2PageResult(executorTaskStatisticsHistoryPageResult);

        return ApiResponse.success(responsePageResult);
    }

    private PageResult<ExecutorTaskDetailResponse> transform2PageResult(PageResult<ExecutorTaskMessage> taskMessagePageResult) {
        List<ExecutorTaskDetailResponse> executorTaskDetailRespons =
                LocalBeanUtils.batchTransform(ExecutorTaskDetailResponse.class,
                        taskMessagePageResult.getContent());

        return new PageResult<>(executorTaskDetailRespons, taskMessagePageResult.getTotal(),
                taskMessagePageResult.getPageNum(),
                taskMessagePageResult.getPageSize());
    }

    private PageResult<ExecutorTaskStatisticsHistoryResponse> transformHistory2PageResult(PageResult<ExecutorTaskStatisticsHistory> executorTaskStatisticsHistoryPageResult) {
        List<ExecutorTaskStatisticsHistoryResponse> historyResponses =
                LocalBeanUtils.batchTransform(ExecutorTaskStatisticsHistoryResponse.class,
                        executorTaskStatisticsHistoryPageResult.getContent());

        return new PageResult<>(historyResponses, executorTaskStatisticsHistoryPageResult.getTotal(),
                executorTaskStatisticsHistoryPageResult.getPageNum(),
                executorTaskStatisticsHistoryPageResult.getPageSize());
    }
}
