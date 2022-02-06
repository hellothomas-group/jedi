package xyz.hellothomas.jedi.demo.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author Thomas
 * @date 2021/8/10 23:17
 * @description
 * @version 1.0
 */
@Slf4j
@Service
public class ScheduledService {
    private static final int SUBMIT_DEFAULT_TASK_CYCLE_SECONDS = 60 * 10;
    private static final int SUBMIT_CUSTOM_TASK_CYCLE_SECONDS = 60 * 30;
    private final TaskService taskService;

    public ScheduledService(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * 定时提交默认任务
     */
    @Scheduled(fixedDelay = 1000 * SUBMIT_DEFAULT_TASK_CYCLE_SECONDS)
    public void submitDefaultTask() {
        long currentTime = System.currentTimeMillis();
        for (int i = 1; i <= 3; i++) {
            taskService.runDefaultTask(currentTime + "-" + i);
        }
        log.info("提交默认任务成功:{}", currentTime);
    }

    /**
     * 定时提交自定义任务
     */
    @Scheduled(fixedDelay = 1000 * SUBMIT_CUSTOM_TASK_CYCLE_SECONDS)
    public void submitCustomTask() {
        long currentTime = System.currentTimeMillis();
        for (int i = 1; i <= 2; i++) {
            taskService.runCustomTask(currentTime + "-" + i);
        }
        log.info("提交自定义任务成功:{}", currentTime);
    }
}
