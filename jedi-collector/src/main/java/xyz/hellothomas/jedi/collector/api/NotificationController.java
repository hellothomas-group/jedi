package xyz.hellothomas.jedi.collector.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.hellothomas.jedi.collector.application.CustomMsgService;
import xyz.hellothomas.jedi.collector.application.ExecutorShutdownMsgService;
import xyz.hellothomas.jedi.collector.application.ExecutorTaskMsgService;
import xyz.hellothomas.jedi.collector.application.ExecutorTickerMsgService;
import xyz.hellothomas.jedi.core.dto.ApiResponse;
import xyz.hellothomas.jedi.core.dto.collector.CustomNotification;
import xyz.hellothomas.jedi.core.dto.collector.ExecutorShutdownNotification;
import xyz.hellothomas.jedi.core.dto.collector.ExecutorTaskNotification;
import xyz.hellothomas.jedi.core.dto.collector.ExecutorTickerNotification;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Thomas
 * @date 2021/1/3 17:21
 * @description
 * @version 1.0
 */
@RestController
@Slf4j
@RequestMapping(value = "/notification")
@Api(value = "notification", tags = "notification")
@Validated
public class NotificationController {
    private final ExecutorTickerMsgService executorTickerMsgService;
    private final ExecutorTaskMsgService executorTaskMsgService;
    private final ExecutorShutdownMsgService executorShutdownMsgService;
    private final CustomMsgService customMsgService;

    public NotificationController(ExecutorTickerMsgService executorTickerMsgService,
                                  ExecutorTaskMsgService executorTaskMsgService,
                                  ExecutorShutdownMsgService executorShutdownMsgService,
                                  CustomMsgService customMsgService) {
        this.executorTickerMsgService = executorTickerMsgService;
        this.executorTaskMsgService = executorTaskMsgService;
        this.executorShutdownMsgService = executorShutdownMsgService;
        this.customMsgService = customMsgService;
    }

    @PostMapping(value = "/executor-ticker")
    @ApiOperation("executor-ticker")
    public ApiResponse<String> executorTick(@Valid @RequestBody List<ExecutorTickerNotification> executorTickerNotifications) {
        executorTickerMsgService.process(executorTickerNotifications);
        return ApiResponse.success("接收成功");
    }

    @PostMapping(value = "/executor-task")
    @ApiOperation("executor-task")
    public ApiResponse<String> executorTask(@Valid @RequestBody List<ExecutorTaskNotification> executorTaskNotifications) {
        executorTaskMsgService.process(executorTaskNotifications);
        return ApiResponse.success("接收成功");
    }

    @PostMapping(value = "/executor-shutdown")
    @ApiOperation("executor-shutdown")
    public ApiResponse<String> executorShutdown(@Valid @RequestBody ExecutorShutdownNotification executorShutdownNotification) {
        executorShutdownMsgService.process(executorShutdownNotification);
        return ApiResponse.success("接收成功");
    }

    @PostMapping(value = "/custom")
    @ApiOperation("custom")
    public ApiResponse<String> defaultNotification(@Valid @RequestBody List<CustomNotification> customNotifications) {
        customNotifications.stream().forEach(i -> customMsgService.process(i));
        return ApiResponse.success("接收成功");
    }
}
