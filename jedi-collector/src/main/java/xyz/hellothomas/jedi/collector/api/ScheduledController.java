package xyz.hellothomas.jedi.collector.api;

import xyz.hellothomas.jedi.collector.application.ScheduledService;

/**
 * @author Thomas
 * @date 2022/5/30 21:00
 * @description
 * @version 1.0
 */
public class ScheduledController {
    private final ScheduledService scheduledService;

    public ScheduledController(ScheduledService scheduledService) {
        this.scheduledService = scheduledService;
    }

    /**
     * 刷新统计当日任务数据
     */
    public void refreshTaskStatistics() {
        scheduledService.refreshTaskStatistics();
    }

    /**
     * 插入D日统计锁记录
     */
    public void insertRefreshTaskStatisticsLock() {
        scheduledService.insertRefreshTaskStatisticsLock();
    }

    /**
     * D日前数据移至历史表
     */
    public void moveStatistics2History() {
        scheduledService.moveStatistics2History();
    }

    /**
     * 刷新统计D-1日数据
     */
    public void refreshLastDayTaskStatistics() {
        scheduledService.refreshTaskStatistics();
    }

    /**
     * 插入D-1日统计锁记录
     */
    public void insertRefreshLastDayTaskStatistics() {
        scheduledService.insertRefreshLastDayTaskStatistics();
    }

    /**
     * D-10日数据清理
     */
    public void clear() {
        scheduledService.clear();
    }
}
