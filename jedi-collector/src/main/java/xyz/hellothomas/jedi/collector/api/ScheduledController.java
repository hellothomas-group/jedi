package xyz.hellothomas.jedi.collector.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import xyz.hellothomas.jedi.collector.application.ScheduledService;

/**
 * @author Thomas
 * @date 2022/5/30 21:00
 * @description
 * @version 1.0
 */
@Api(value = "scheduled", tags = "scheduled")
@RestController
@RequestMapping("/scheduled")
@Slf4j
public class ScheduledController {
    private final ScheduledService scheduledService;

    public ScheduledController(ScheduledService scheduledService) {
        this.scheduledService = scheduledService;
    }

    /**
     * 刷新统计当日任务数据
     */
    @PutMapping(value = "/refresh-task-statistics")
    @ApiOperation("refreshTaskStatistics")
    public void refreshTaskStatistics() {
        scheduledService.refreshTaskStatistics();
    }

    /**
     * 插入D日统计锁记录
     */
    @PostMapping(value = "/insert-refresh-task-statistics-lock")
    @ApiOperation("insertRefreshTaskStatisticsLock")
    public void insertRefreshTaskStatisticsLock() {
        scheduledService.insertRefreshTaskStatisticsLock();
    }

    /**
     * D日前数据移至历史表
     */
    @PostMapping(value = "/move-statistics-to-history")
    @ApiOperation("moveStatistics2History")
    public void moveStatistics2History() {
        scheduledService.moveStatistics2History();
    }

    /**
     * 刷新统计D-1日数据
     */
    @PutMapping(value = "/refresh-last-day-task-statistics")
    @ApiOperation("refreshLastDayTaskStatistics")
    public void refreshLastDayTaskStatistics() {
        scheduledService.refreshTaskStatistics();
    }

    /**
     * 插入D-1日统计锁记录
     */
    @PostMapping(value = "/insert-refresh-last-day-task-statistics")
    @ApiOperation("insertRefreshLastDayTaskStatistics")
    public void insertRefreshLastDayTaskStatistics() {
        scheduledService.insertRefreshLastDayTaskStatistics();
    }

    /**
     * D-10日数据清理
     */
    @DeleteMapping(value = "/clear")
    @ApiOperation("clear")
    public void clear() {
        scheduledService.clear();
    }
}
