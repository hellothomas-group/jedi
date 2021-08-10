package xyz.hellothomas.jedi.consumer.application;

import org.springframework.stereotype.Service;

/**
 * @author Thomas
 * @date 2021/8/10 23:17
 * @description
 * @version 1.0
 */
@Service
public class ScheduledService {
    private final ExecutorTaskStatisticsHistoryService taskStatisticsHistoryService;
    private final ExecutorTaskStatisticsService taskStatisticsService;

    public ScheduledService(ExecutorTaskStatisticsHistoryService taskStatisticsHistoryService,
                            ExecutorTaskStatisticsService taskStatisticsService) {
        this.taskStatisticsHistoryService = taskStatisticsHistoryService;
        this.taskStatisticsService = taskStatisticsService;
    }
}
