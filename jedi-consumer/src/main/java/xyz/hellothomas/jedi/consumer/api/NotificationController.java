package xyz.hellothomas.jedi.consumer.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.hellothomas.jedi.consumer.application.CustomMessageService;
import xyz.hellothomas.jedi.consumer.application.ExecutorShutdownService;
import xyz.hellothomas.jedi.consumer.application.ExecutorTaskService;
import xyz.hellothomas.jedi.consumer.application.ExecutorTickerService;
import xyz.hellothomas.jedi.core.dto.ApiResponse;
import xyz.hellothomas.jedi.core.dto.consumer.CustomNotification;
import xyz.hellothomas.jedi.core.dto.consumer.ExecutorShutdownNotification;
import xyz.hellothomas.jedi.core.dto.consumer.ExecutorTaskNotification;
import xyz.hellothomas.jedi.core.dto.consumer.ExecutorTickerNotification;

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
    private final ExecutorTickerService executorTickerService;
    private final ExecutorTaskService executorTaskService;
    private final ExecutorShutdownService executorShutdownService;
    private final CustomMessageService customMessageService;

    public NotificationController(ExecutorTickerService executorTickerService,
                                  ExecutorTaskService executorTaskService,
                                  ExecutorShutdownService executorShutdownService,
                                  CustomMessageService customMessageService) {
        this.executorTickerService = executorTickerService;
        this.executorTaskService = executorTaskService;
        this.executorShutdownService = executorShutdownService;
        this.customMessageService = customMessageService;
    }

    @PostMapping(value = "/executor-ticker")
    @ApiOperation("executor-ticker")
    public ApiResponse<String> executorTick(@Valid @RequestBody List<ExecutorTickerNotification> executorTickerNotifications) {
        executorTickerService.process(executorTickerNotifications);
        return ApiResponse.success("接收成功");
    }

    @PostMapping(value = "/executor-task")
    @ApiOperation("executor-task")
    public ApiResponse<String> executorTask(@Valid @RequestBody List<ExecutorTaskNotification> executorTaskNotifications) {
        executorTaskService.process(executorTaskNotifications);
        return ApiResponse.success("接收成功");
    }

    @PostMapping(value = "/executor-shutdown")
    @ApiOperation("executor-shutdown")
    public ApiResponse<String> executorShutdown(@Valid @RequestBody ExecutorShutdownNotification executorShutdownNotification) {
        executorShutdownService.process(executorShutdownNotification);
        return ApiResponse.success("接收成功");
    }

    @PostMapping(value = "/custom")
    @ApiOperation("custom")
    public ApiResponse<String> defaultNotification(@Valid @RequestBody List<CustomNotification> customNotifications) {
        customNotifications.stream().forEach(i -> customMessageService.process(i));
        return ApiResponse.success("接收成功");
    }
}
