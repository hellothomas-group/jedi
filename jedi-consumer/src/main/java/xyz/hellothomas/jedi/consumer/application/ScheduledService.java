package xyz.hellothomas.jedi.consumer.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import xyz.hellothomas.jedi.biz.common.utils.LocalBeanUtils;
import xyz.hellothomas.jedi.biz.domain.monitor.App;
import xyz.hellothomas.jedi.consumer.api.dto.PageHelperRequest;
import xyz.hellothomas.jedi.consumer.api.dto.PageResult;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTask;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskStatistics;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskStatisticsHistory;
import xyz.hellothomas.jedi.consumer.domain.TaskLock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Thomas
 * @date 2021/8/10 23:17
 * @description
 * @version 1.0
 */
@Slf4j
@Service
public class ScheduledService {
    private static final String CLEAR_CLIENT_MESSAGE_NAME = "CLEAR_CLIENT_MESSAGE";
    private static final String REFRESH_TASK_STATISTICS_NAME = "REFRESH_TASK_STATISTICS";
    private static final String MOVE_HISTORY_TASK_STATISTICS_NAME = "MOVE_HISTORY_TASK_STATISTICS";
    private static final int REFRESH_TASK_STATISTICS_CYCLE_SECONDS = 60 * 2;
    private static final String REFRESH_LAST_DAY_TASK_STATISTICS_NAME = "REFRESH_LAST_DAY_TASK_STATISTICS";
    private static final int REFRESH_TASK_STATISTICS_INTERVAL_SECONDS = 1800;

    private final ExecutorTaskStatisticsHistoryService taskStatisticsHistoryService;
    private final ExecutorTaskStatisticsService taskStatisticsService;
    private final ClearDataService clearDataService;
    private final TaskLockService taskLockService;
    private final ExecutorTaskMsgService executorTaskMsgService;
    private final AppService appService;
    private final ExecutorTaskService executorTaskService;

    public ScheduledService(ExecutorTaskStatisticsHistoryService taskStatisticsHistoryService,
                            ExecutorTaskStatisticsService taskStatisticsService, ClearDataService clearDataService,
                            TaskLockService taskLockService, ExecutorTaskMsgService executorTaskMsgService,
                            AppService appService, ExecutorTaskService executorTaskService) {
        this.taskStatisticsHistoryService = taskStatisticsHistoryService;
        this.taskStatisticsService = taskStatisticsService;
        this.clearDataService = clearDataService;
        this.taskLockService = taskLockService;
        this.executorTaskMsgService = executorTaskMsgService;
        this.appService = appService;
        this.executorTaskService = executorTaskService;
    }

    /**
     * 刷新统计当日任务数据
     */
    @Scheduled(fixedDelay = 1000 * REFRESH_TASK_STATISTICS_CYCLE_SECONDS)
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor =
            Exception.class)
    public void refreshTaskStatistics() {
        LocalDate currentDate = LocalDate.now();
        TaskLock taskLock = taskLockService.selectByTaskDateAndTaskName(currentDate, REFRESH_TASK_STATISTICS_NAME);
        if (taskLock == null ||
                taskLock.getDataChangeLastModifiedTime().isAfter(LocalDateTime.now().minusSeconds(REFRESH_TASK_STATISTICS_CYCLE_SECONDS))) {
            return;
        }

        // 悲观锁锁当天刷新任务
        try {
            taskLock = taskLockService.lock(taskLock.getId());
        } catch (Exception e) {
            log.info("悲观锁 {} 获取失败", REFRESH_TASK_STATISTICS_NAME);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return;
        }
        if (taskLock == null ||
                taskLock.getDataChangeLastModifiedTime().isAfter(LocalDateTime.now().minusSeconds(REFRESH_TASK_STATISTICS_CYCLE_SECONDS))) {
            return;
        }

        // 获取apps
        List<App> apps = appService.findAppIds();

        apps.stream().forEach(app -> {
            // 获取tasks
            List<ExecutorTask> executorTasks = executorTaskService.findTaskList(app.getNamespaceName(),
                    app.getAppId());

            // 计算各task统计数并更新
            executorTasks.stream().forEach(i -> {
                log.info("executorTask:{}", i);

                if (i.getIsDeleted()) {
                    return;
                }

                ExecutorTaskStatistics executorTaskStatisticsOrigin = taskStatisticsService.findOne(i.getNamespaceName()
                        , i.getAppId(), i.getExecutorName(), i.getTaskName(), currentDate);

                ExecutorTaskStatistics executorTaskStatistics =
                        executorTaskMsgService.genTaskStatistics(i.getNamespaceName()
                                , i.getAppId(),
                                i.getExecutorName(),
                                i.getTaskName(), currentDate.atStartOfDay(), currentDate.plusDays(1).atStartOfDay());
                log.info("executorTaskStatistics:{}", executorTaskStatistics);
                if (executorTaskStatistics.getTotal() == 0) {
                    executorTaskStatistics.setFailureRatio(new BigDecimal(0));
                } else {
                    executorTaskStatistics.setFailureRatio(new BigDecimal(executorTaskStatistics.getFailure())
                            .divide(new BigDecimal(executorTaskStatistics.getTotal()), 2, RoundingMode.HALF_UP));
                }

                LocalDateTime currentDateTime = LocalDateTime.now();
                if (executorTaskStatisticsOrigin == null) {
                    executorTaskStatistics.setStatisticsDate(currentDate);
                    executorTaskStatistics.setDataChangeCreatedTime(currentDateTime);
                    executorTaskStatistics.setDataChangeLastModifiedTime(currentDateTime);
                    executorTaskStatistics.setVersion(1);
                    taskStatisticsService.insertSelective(executorTaskStatistics);
                } else {
                    executorTaskStatistics.setId(executorTaskStatisticsOrigin.getId());
                    executorTaskStatistics.setDataChangeCreatedTime(executorTaskStatisticsOrigin.getDataChangeCreatedTime());
                    executorTaskStatistics.setVersion(executorTaskStatisticsOrigin.getVersion() + 1);
                    executorTaskStatistics.setDataChangeLastModifiedTime(currentDateTime);
                    taskStatisticsService.updateByPrimaryKeySelective(executorTaskStatistics);
                }
            });
        });

        // 更新刷新任务
        taskLockService.updateModifiedTimeAndVersion(taskLock);
    }

    /**
     * 插入D日统计锁记录
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void insertRefreshTaskStatisticsLock() {
        LocalDate currentDate = LocalDate.now();
        // 创建D日刷新任务
        if (taskLockService.insertTaskLock(currentDate, REFRESH_TASK_STATISTICS_NAME) == 0) {
            return;
        }
        log.info("创建{}插入D日统计锁记录成功", currentDate);
    }

    /**
     * D日前数据移至历史表
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor =
            Exception.class)
    public void moveStatistics2History() {
        LocalDate currentDate = LocalDate.now();
        // 创建D日过历史任务
        if (taskLockService.insertTaskLock(currentDate, MOVE_HISTORY_TASK_STATISTICS_NAME) == 0) {
            return;
        }
        log.info("创建{}前数据移至历史表任务成功", currentDate);

        // D日前统计数据复制到历史表
        PageHelperRequest pageHelperRequest = new PageHelperRequest();
        pageHelperRequest.setPageNum(1);
        pageHelperRequest.setPageSize(1000);
        while (true) {
            PageResult<ExecutorTaskStatistics> pageResult = taskStatisticsService.findList(currentDate,
                    pageHelperRequest);
            pageResult.getContent().stream().forEach(i -> {
                ExecutorTaskStatisticsHistory executorTaskStatisticsHistory =
                        LocalBeanUtils.transform(ExecutorTaskStatisticsHistory.class, i);
                // 复制到历史表
                taskStatisticsHistoryService.insertOne(executorTaskStatisticsHistory);
                // 删除数据
                taskStatisticsService.deleteByPrimaryKey(i.getId());
            });
            if (pageResult.getTotal() <= pageResult.getPageNum() * pageResult.getPageSize()) {
                break;
            } else {
                pageHelperRequest.setPageNum(pageResult.getPageNum() + 1);
            }
        }
    }

    /**
     * 刷新统计D-1日数据
     */
    @Scheduled(cron = "0 0 1,3,5,9,17,23 * * ?")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor =
            Exception.class)
    public void refreshLastDayTaskStatistics() {
        LocalDate currentDate = LocalDate.now();
        LocalDate lastDate = currentDate.minusDays(1);
        TaskLock taskLock = taskLockService.selectByTaskDateAndTaskName(currentDate,
                REFRESH_LAST_DAY_TASK_STATISTICS_NAME);
        if (taskLock == null ||
                taskLock.getDataChangeLastModifiedTime().isAfter(LocalDateTime.now().minusSeconds(REFRESH_TASK_STATISTICS_INTERVAL_SECONDS))) {
            return;
        }

        // 悲观锁锁D-1刷新任务
        try {
            taskLock = taskLockService.lock(taskLock.getId());
        } catch (Exception e) {
            log.info("悲观锁 {} 获取失败", REFRESH_LAST_DAY_TASK_STATISTICS_NAME);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return;
        }

        if (taskLock == null ||
                taskLock.getDataChangeLastModifiedTime().isAfter(LocalDateTime.now().minusSeconds(REFRESH_TASK_STATISTICS_INTERVAL_SECONDS))) {
            return;
        }

        // 获取apps
        List<App> apps = appService.findAppIds();

        apps.stream().forEach(app -> {
            // 获取tasks
            List<ExecutorTask> executorTasks = executorTaskService.findTaskList(app.getNamespaceName(), app.getAppId());

            // 计算各task统计数并更新
            executorTasks.stream().forEach(i -> {
                log.info("executorTask:{}", i);

                ExecutorTaskStatisticsHistory executorTaskStatisticsHistoryOrigin =
                        taskStatisticsHistoryService.findOne(i.getNamespaceName(),
                                i.getAppId(),
                                i.getExecutorName(), i.getTaskName(), lastDate);

                if (executorTaskStatisticsHistoryOrigin == null) {
                    return;
                }

                ExecutorTaskStatistics executorTaskStatistics =
                        executorTaskMsgService.genTaskStatistics(i.getNamespaceName()
                                , i.getAppId(),
                                i.getExecutorName(),
                                i.getTaskName(), lastDate.atStartOfDay(), currentDate.atStartOfDay());
                ExecutorTaskStatisticsHistory executorTaskStatisticsHistory =
                        LocalBeanUtils.transform(ExecutorTaskStatisticsHistory.class, executorTaskStatistics);
                log.info("executorTaskStatisticsHistory:{}", executorTaskStatisticsHistory);
                if (executorTaskStatisticsHistory.getTotal() == 0) {
                    executorTaskStatisticsHistory.setFailureRatio(new BigDecimal(0));
                } else {
                    executorTaskStatisticsHistory.setFailureRatio(new BigDecimal(executorTaskStatisticsHistory.getFailure())
                            .divide(new BigDecimal(executorTaskStatisticsHistory.getTotal()), 2, RoundingMode.HALF_UP));
                }

                LocalDateTime currentDateTime = LocalDateTime.now();
                executorTaskStatisticsHistory.setId(executorTaskStatisticsHistoryOrigin.getId());
                executorTaskStatisticsHistory.setDataChangeCreatedTime(executorTaskStatisticsHistoryOrigin.getDataChangeCreatedTime());
                executorTaskStatisticsHistory.setVersion(executorTaskStatisticsHistoryOrigin.getVersion() + 1);
                executorTaskStatisticsHistory.setDataChangeLastModifiedTime(currentDateTime);
                taskStatisticsHistoryService.updateByPrimaryKeySelective(executorTaskStatisticsHistory);
            });
        });

        // 更新刷新任务
        taskLockService.updateModifiedTimeAndVersion(taskLock);
    }

    /**
     * 插入D-1日统计锁记录
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void insertRefreshLastDayTaskStatistics() {
        LocalDate currentDate = LocalDate.now();
        // 创建D日刷新任务
        if (taskLockService.insertTaskLock(currentDate, REFRESH_LAST_DAY_TASK_STATISTICS_NAME) == 0) {
            return;
        }
        log.info("创建{}插入D-1日统计锁记录成功", currentDate);
    }

    /**
     * D-10日数据清理
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void clear() {
        LocalDate currentDate = LocalDate.now();
        // 创建D日数据清理任务
        if (taskLockService.insertTaskLock(currentDate, CLEAR_CLIENT_MESSAGE_NAME) == 0) {
            return;
        }
        log.info("创建{}数据清理任务成功", currentDate);

        // 清理D-10日客户端消息
        clearDataService.clearExecutorTickMessage(currentDate);
        clearDataService.clearExecutorTaskMessage(currentDate);
        clearDataService.clearExecutorShutdownMessage(currentDate);
        clearDataService.clearMonitorMessage(currentDate);
    }
}
