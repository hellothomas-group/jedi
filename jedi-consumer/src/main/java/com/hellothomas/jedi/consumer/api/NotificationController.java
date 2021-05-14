package com.hellothomas.jedi.consumer.api;

import com.hellothomas.jedi.consumer.application.CustomMessageService;
import com.hellothomas.jedi.consumer.application.ExecutorShutdownService;
import com.hellothomas.jedi.consumer.application.ExecutorTaskService;
import com.hellothomas.jedi.consumer.application.ExecutorTickerService;
import com.hellothomas.jedi.core.dto.ReturnT;
import com.hellothomas.jedi.core.dto.consumer.CustomNotification;
import com.hellothomas.jedi.core.dto.consumer.ExecutorShutdownNotification;
import com.hellothomas.jedi.core.dto.consumer.ExecutorTaskNotification;
import com.hellothomas.jedi.core.dto.consumer.ExecutorTickerNotification;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @className NotificationController
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
    public ReturnT<String> executorTick(@Valid @RequestBody List<ExecutorTickerNotification> executorTickerNotifications) {
        log.info("executorTickerNotifications: {}", executorTickerNotifications);
        executorTickerNotifications.stream().forEach(i -> executorTickerService.save(i));
        return ReturnT.SUCCESS;
    }

    @PostMapping(value = "/executor-task")
    @ApiOperation("executor-task")
    public ReturnT<String> executorTask(@Valid @RequestBody List<ExecutorTaskNotification> executorTaskNotifications) {
        log.info("executorTaskNotifications: {}", executorTaskNotifications);
        executorTaskNotifications.stream().forEach(i -> executorTaskService.save(i));
        return ReturnT.SUCCESS;
    }

    @PostMapping(value = "/executor-shutdown")
    @ApiOperation("executor-shutdown")
    public ReturnT<String> executorShutdown(@Valid @RequestBody ExecutorShutdownNotification executorShutdownNotification) {
        log.info("executorShutdownNotification: {}", executorShutdownNotification);
        executorShutdownService.save(executorShutdownNotification);
        return ReturnT.SUCCESS;
    }

    @PostMapping(value = "/custom")
    @ApiOperation("custom")
    public ReturnT<String> defaultNotification(@Valid @RequestBody List<CustomNotification> customNotifications) {
        log.info("customNotifications: {}", customNotifications);
        customNotifications.stream().forEach(i -> customMessageService.save(i));
        return ReturnT.SUCCESS;
    }
}
