package xyz.hellothomas.jedi.collector.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import xyz.hellothomas.jedi.biz.common.utils.LocalBeanUtils;
import xyz.hellothomas.jedi.biz.domain.monitor.App;
import xyz.hellothomas.jedi.collector.api.dto.PageHelperRequest;
import xyz.hellothomas.jedi.collector.api.dto.PageResult;
import xyz.hellothomas.jedi.collector.domain.ExecutorTask;
import xyz.hellothomas.jedi.collector.domain.ExecutorTaskStatistics;
import xyz.hellothomas.jedi.collector.domain.ExecutorTaskStatisticsHistory;
import xyz.hellothomas.jedi.collector.domain.TaskLock;

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
     * ??????????????????????????????
     */
    @Scheduled(fixedDelay = 1000 * REFRESH_TASK_STATISTICS_CYCLE_SECONDS)
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor =
            Exception.class)
    public void refreshTaskStatistics() {
        LocalDate currentDate = LocalDate.now();
        TaskLock taskLock = taskLockService.selectByTaskDateAndTaskName(currentDate, REFRESH_TASK_STATISTICS_NAME);
        if (taskLock == null ||
                taskLock.getUpdateTime().isAfter(LocalDateTime.now().minusSeconds(REFRESH_TASK_STATISTICS_CYCLE_SECONDS))) {
            return;
        }

        // ??????????????????????????????
        try {
            taskLock = taskLockService.lock(taskLock.getId());
        } catch (Exception e) {
            log.info("????????? {} ????????????", REFRESH_TASK_STATISTICS_NAME);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return;
        }
        if (taskLock == null ||
                taskLock.getUpdateTime().isAfter(LocalDateTime.now().minusSeconds(REFRESH_TASK_STATISTICS_CYCLE_SECONDS))) {
            return;
        }

        // ??????apps
        List<App> apps = appService.findAppIds();

        apps.stream().forEach(app -> {
            // ??????tasks
            List<ExecutorTask> executorTasks = executorTaskService.findTaskList(app.getNamespaceName(),
                    app.getAppId());

            // ?????????task??????????????????
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
                    executorTaskStatistics.setCreateTime(currentDateTime);
                    executorTaskStatistics.setUpdateTime(currentDateTime);
                    executorTaskStatistics.setVersion(1);
                    taskStatisticsService.insertSelective(executorTaskStatistics);
                } else {
                    executorTaskStatistics.setId(executorTaskStatisticsOrigin.getId());
                    executorTaskStatistics.setCreateTime(executorTaskStatisticsOrigin.getCreateTime());
                    executorTaskStatistics.setVersion(executorTaskStatisticsOrigin.getVersion() + 1);
                    executorTaskStatistics.setUpdateTime(currentDateTime);
                    taskStatisticsService.updateByPrimaryKeySelective(executorTaskStatistics);
                }
            });
        });

        // ??????????????????
        taskLockService.updateModifiedTimeAndVersion(taskLock);
    }

    /**
     * ??????D??????????????????
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void insertRefreshTaskStatisticsLock() {
        LocalDate currentDate = LocalDate.now();
        // ??????D???????????????
        if (taskLockService.insertTaskLock(currentDate, REFRESH_TASK_STATISTICS_NAME) == 0) {
            return;
        }
        log.info("??????{}??????D????????????????????????", currentDate);
    }

    /**
     * D???????????????????????????
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor =
            Exception.class)
    public void moveStatistics2History() {
        LocalDate currentDate = LocalDate.now();
        // ??????D??????????????????
        if (taskLockService.insertTaskLock(currentDate, MOVE_HISTORY_TASK_STATISTICS_NAME) == 0) {
            return;
        }
        log.info("??????{}????????????????????????????????????", currentDate);

        // D????????????????????????????????????
        PageHelperRequest pageHelperRequest = new PageHelperRequest();
        pageHelperRequest.setPageNum(1);
        pageHelperRequest.setPageSize(1000);
        while (true) {
            PageResult<ExecutorTaskStatistics> pageResult = taskStatisticsService.findList(currentDate,
                    pageHelperRequest);
            pageResult.getContent().stream().forEach(i -> {
                ExecutorTaskStatisticsHistory executorTaskStatisticsHistory =
                        LocalBeanUtils.transform(ExecutorTaskStatisticsHistory.class, i);
                // ??????????????????
                taskStatisticsHistoryService.insertOne(executorTaskStatisticsHistory);
                // ????????????
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
     * ????????????D-1?????????
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
                taskLock.getUpdateTime().isAfter(LocalDateTime.now().minusSeconds(REFRESH_TASK_STATISTICS_INTERVAL_SECONDS))) {
            return;
        }

        // ????????????D-1????????????
        try {
            taskLock = taskLockService.lock(taskLock.getId());
        } catch (Exception e) {
            log.info("????????? {} ????????????", REFRESH_LAST_DAY_TASK_STATISTICS_NAME);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return;
        }

        if (taskLock == null ||
                taskLock.getUpdateTime().isAfter(LocalDateTime.now().minusSeconds(REFRESH_TASK_STATISTICS_INTERVAL_SECONDS))) {
            return;
        }

        // ??????apps
        List<App> apps = appService.findAppIds();

        apps.stream().forEach(app -> {
            // ??????tasks
            List<ExecutorTask> executorTasks = executorTaskService.findTaskList(app.getNamespaceName(), app.getAppId());

            // ?????????task??????????????????
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
                executorTaskStatisticsHistory.setCreateTime(executorTaskStatisticsHistoryOrigin.getCreateTime());
                executorTaskStatisticsHistory.setVersion(executorTaskStatisticsHistoryOrigin.getVersion() + 1);
                executorTaskStatisticsHistory.setUpdateTime(currentDateTime);
                taskStatisticsHistoryService.updateByPrimaryKeySelective(executorTaskStatisticsHistory);
            });
        });

        // ??????????????????
        taskLockService.updateModifiedTimeAndVersion(taskLock);
    }

    /**
     * ??????D-1??????????????????
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void insertRefreshLastDayTaskStatistics() {
        LocalDate currentDate = LocalDate.now();
        // ??????D???????????????
        if (taskLockService.insertTaskLock(currentDate, REFRESH_LAST_DAY_TASK_STATISTICS_NAME) == 0) {
            return;
        }
        log.info("??????{}??????D-1????????????????????????", currentDate);
    }

    /**
     * D-10???????????????
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void clear() {
        LocalDate currentDate = LocalDate.now();
        // ??????D?????????????????????
        if (taskLockService.insertTaskLock(currentDate, CLEAR_CLIENT_MESSAGE_NAME) == 0) {
            return;
        }
        log.info("??????{}????????????????????????", currentDate);

        // ??????D-10??????????????????
        clearDataService.clearExecutorTickMessage(currentDate);
        clearDataService.clearExecutorTaskMessage(currentDate);
        clearDataService.clearExecutorShutdownMessage(currentDate);
        clearDataService.clearMonitorMessage(currentDate);
    }
}
