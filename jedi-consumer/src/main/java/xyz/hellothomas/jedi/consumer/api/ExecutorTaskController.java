package xyz.hellothomas.jedi.consumer.api;

import io.swagger.annotations.Api;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import xyz.hellothomas.jedi.biz.common.utils.LocalBeanUtils;
import xyz.hellothomas.jedi.consumer.api.dto.*;
import xyz.hellothomas.jedi.consumer.application.ExecutorTaskMsgService;
import xyz.hellothomas.jedi.consumer.application.ExecutorTaskStatisticsHistoryService;
import xyz.hellothomas.jedi.consumer.application.ExecutorTaskStatisticsService;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskMessage;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskStatistics;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskStatisticsHistory;
import xyz.hellothomas.jedi.consumer.domain.pojo.ExecutorTaskSummary;
import xyz.hellothomas.jedi.core.dto.ApiResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Api(value = "executor-task", tags = "executor-task")
@RestController
public class ExecutorTaskController {

    private final ExecutorTaskMsgService executorTaskMsgService;
    private final ExecutorTaskStatisticsService executorTaskStatisticsService;
    private final ExecutorTaskStatisticsHistoryService executorTaskStatisticsHistoryService;

    public ExecutorTaskController(ExecutorTaskMsgService executorTaskMsgService,
                                  ExecutorTaskStatisticsService executorTaskStatisticsService,
                                  ExecutorTaskStatisticsHistoryService executorTaskStatisticsHistoryService) {
        this.executorTaskMsgService = executorTaskMsgService;
        this.executorTaskStatisticsService = executorTaskStatisticsService;
        this.executorTaskStatisticsHistoryService = executorTaskStatisticsHistoryService;
    }

    @GetMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/task-details")
    public ApiResponse<PageResult<ExecutorTaskDetailResponse>> findTaskDetail(@PathVariable("namespaceName") String namespaceName,
                                                                              @PathVariable("appId") String appId,
                                                                              @PathVariable("executorName") String executorName,
                                                                              @RequestParam(value = "taskName") String taskName,
                                                                              @RequestParam(value = "taskExtraData",
                                                                                      required = false) String taskExtraData,
                                                                              @RequestParam(value = "isSuccess",
                                                                                      required =
                                                                                              false) Boolean isSuccess,
                                                                              @RequestParam(value = "instanceIp",
                                                                                      required =
                                                                                              false) String instanceIp,
                                                                              @RequestParam("startTime") @DateTimeFormat(pattern = "yyyy-MM-dd " +
                                                                                      "HH:mm:ss") LocalDateTime startTime,
                                                                              @RequestParam("endTime") @DateTimeFormat(pattern
                                                                                      = "yyyy-MM-dd " +
                                                                                      "HH:mm:ss") LocalDateTime endTime,
                                                                              PageHelperRequest pageHelperRequest) {
        PageResult<ExecutorTaskMessage> taskMessagePageResult =
                executorTaskMsgService.findByTaskNameAndRecordTime(namespaceName
                        , appId, executorName, taskName, taskExtraData, isSuccess, instanceIp, startTime, endTime,
                        pageHelperRequest);
        PageResult<ExecutorTaskDetailResponse> executorStatusResponsePageResult =
                transform2PageResult(taskMessagePageResult);

        return ApiResponse.success(executorStatusResponsePageResult);
    }

    @GetMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/task/{taskName}/statistics")
    public ApiResponse<ExecutorTaskStatisticsResponse> findTaskStatistics(@PathVariable("namespaceName") String namespaceName,
                                                                          @PathVariable("appId") String appId,
                                                                          @PathVariable("executorName") String executorName,
                                                                          @PathVariable("taskName") String taskName,
                                                                          @RequestParam("statisticsDate")
                                                                          @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate statisticsDate) {
        ExecutorTaskStatistics executorTaskStatistics =
                executorTaskStatisticsService.findOne(namespaceName, appId, executorName, taskName, statisticsDate);

        return ApiResponse.success(LocalBeanUtils.transform(ExecutorTaskStatisticsResponse.class,
                executorTaskStatistics));
    }

    @GetMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/task-statistics-history/all")
    public ApiResponse<PageResult<ExecutorTaskStatisticsHistoryResponse>> findTaskStatisticsHistoryList(
            @PathVariable("namespaceName") String namespaceName,
            @PathVariable("appId") String appId,
            @PathVariable("executorName") String executorName,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(value = "taskName", required = false) String taskName,
            PageHelperRequest pageHelperRequest) {
        PageResult<ExecutorTaskStatisticsHistory> executorTaskStatisticsHistoryPageResult =
                executorTaskStatisticsHistoryService.findList(namespaceName, appId, executorName, startDate,
                        endDate, taskName, pageHelperRequest);
        PageResult<ExecutorTaskStatisticsHistoryResponse> responsePageResult =
                transformHistory2PageResult(executorTaskStatisticsHistoryPageResult);

        return ApiResponse.success(responsePageResult);
    }

    @GetMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/task-statistics/all")
    public ApiResponse<PageResult<ExecutorTaskStatisticsResponse>> findTaskStatisticsList(
            @PathVariable("namespaceName") String namespaceName,
            @PathVariable("appId") String appId,
            @PathVariable("executorName"
            ) String executorName,
            PageHelperRequest pageHelperRequest) {
        PageResult<ExecutorTaskStatistics> executorTaskStatisticsPageResult =
                executorTaskStatisticsService.findList(namespaceName, appId, executorName, LocalDate.now(),
                        pageHelperRequest);

        PageResult<ExecutorTaskStatisticsResponse> responsePageResult =
                transformStatistics2PageResult(executorTaskStatisticsPageResult);

        return ApiResponse.success(responsePageResult);
    }

    @GetMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/task-summary")
    public ApiResponse<ExecutorTaskSummaryResponse> findExecutorTaskSummary(@PathVariable("namespaceName") String namespaceName,
                                                                            @PathVariable("appId") String appId,
                                                                            @PathVariable("executorName") String executorName,
                                                                            @RequestParam("statisticsDate")
                                                                            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate statisticsDate) {
        ExecutorTaskSummary executorTaskSummary =
                executorTaskStatisticsService.summaryTaskStatistics(namespaceName, appId, executorName, statisticsDate);

        return ApiResponse.success(LocalBeanUtils.transform(ExecutorTaskSummaryResponse.class,
                executorTaskSummary));
    }

    @PostMapping("/tasks/{taskId}/retried")
    public ApiResponse<String> updateRetriedTask(@PathVariable("taskId") String taskId,
                                                 @RequestParam("newTaskId") String newTaskId,
                                                 @RequestParam("operator") String operator) {
        executorTaskMsgService.updateRetriedTask(taskId, newTaskId, operator);
        return ApiResponse.success("更新成功");
    }

    private PageResult<ExecutorTaskStatisticsResponse> transformStatistics2PageResult(
            PageResult<ExecutorTaskStatistics> executorTaskStatisticsPageResult) {
        List<ExecutorTaskStatisticsResponse> statisticsResponses =
                LocalBeanUtils.batchTransform(ExecutorTaskStatisticsResponse.class,
                        executorTaskStatisticsPageResult.getContent());

        return new PageResult<>(statisticsResponses, executorTaskStatisticsPageResult.getTotal(),
                executorTaskStatisticsPageResult.getPageNum(),
                executorTaskStatisticsPageResult.getPageSize());
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
