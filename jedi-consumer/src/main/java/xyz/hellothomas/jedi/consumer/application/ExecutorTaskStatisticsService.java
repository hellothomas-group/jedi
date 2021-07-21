package xyz.hellothomas.jedi.consumer.application;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import xyz.hellothomas.jedi.consumer.api.dto.PageHelperRequest;
import xyz.hellothomas.jedi.consumer.api.dto.PageResult;
import xyz.hellothomas.jedi.consumer.domain.*;
import xyz.hellothomas.jedi.consumer.domain.pojo.ExecutorTask;
import xyz.hellothomas.jedi.consumer.infrastructure.mapper.ExecutorTaskStatisticsHistoryMapper;
import xyz.hellothomas.jedi.consumer.infrastructure.mapper.ExecutorTaskStatisticsMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static xyz.hellothomas.jedi.consumer.common.constants.Constants.DEFAULT_PAGE_SIZE;

/**
 * @author Thomas
 * @date 2021/3/7 23:18
 * @description
 * @version 1.0
 */
@Slf4j
@Service
public class ExecutorTaskStatisticsService {
    private static final String REFRESH_TASK_STATISTICS_NAME = "REFRESH_TASK_STATISTICS";
    private static final int REFRESH_TASK_STATISTICS_CYCLE_SECONDS = 60 * 2;
    private final ExecutorTaskStatisticsMapper executorTaskStatisticsMapper;
    private final ExecutorTaskStatisticsHistoryMapper executorTaskStatisticsHistoryMapper;
    private final ExecutorTaskService executorTaskService;
    private final TaskLockService taskLockService;

    public ExecutorTaskStatisticsService(ExecutorTaskStatisticsMapper executorTaskStatisticsMapper,
                                         ExecutorTaskStatisticsHistoryMapper executorTaskStatisticsHistoryMapper,
                                         ExecutorTaskService executorTaskService, TaskLockService taskLockService) {
        this.executorTaskStatisticsMapper = executorTaskStatisticsMapper;
        this.executorTaskStatisticsHistoryMapper = executorTaskStatisticsHistoryMapper;
        this.executorTaskService = executorTaskService;
        this.taskLockService = taskLockService;
    }

    public ExecutorTaskStatistics findOne(String namespaceName, String appId, String executorName, String taskName,
                                          LocalDate statisticsDate) {
        ExecutorTaskStatisticsExample executorTaskStatisticsExample = new ExecutorTaskStatisticsExample();
        executorTaskStatisticsExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdEqualTo(appId)
                .andExecutorNameEqualTo(executorName)
                .andTaskNameEqualTo(taskName)
                .andStatisticsDateEqualTo(statisticsDate);

        List<ExecutorTaskStatistics> executorTaskStatisticsList =
                executorTaskStatisticsMapper.selectByExample(executorTaskStatisticsExample);

        return executorTaskStatisticsList.isEmpty() ? null : executorTaskStatisticsList.get(0);
    }

    public PageResult<ExecutorTaskStatistics> findList(String namespaceName, String appId, String executorName,
                                                       LocalDate statisticsDate, PageHelperRequest pageHelperRequest) {
        ExecutorTaskStatisticsExample executorTaskStatisticsExample = new ExecutorTaskStatisticsExample();
        executorTaskStatisticsExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdEqualTo(appId)
                .andExecutorNameEqualTo(executorName)
                .andStatisticsDateEqualTo(statisticsDate);

        int pageSize = pageHelperRequest.getPageSize();
        int pageNum = pageHelperRequest.getPageNum();
        pageSize = (pageSize <= 0) ? DEFAULT_PAGE_SIZE : pageSize;
        PageHelper.startPage(pageNum, pageSize);

        List<ExecutorTaskStatistics> executorTaskStatisticsList =
                executorTaskStatisticsMapper.selectByExample(executorTaskStatisticsExample);

        PageInfo<ExecutorTaskStatistics> pageInfo = new PageInfo<>(executorTaskStatisticsList);

        return PageResult.<ExecutorTaskStatistics>builder()
                .content(pageInfo.getList())
                .total(pageInfo.getTotal())
                .pageNum(pageInfo.getPageNum())
                .pageSize(pageInfo.getPageSize())
                .build();
    }

    public PageResult<ExecutorTaskStatisticsHistory> findHistory(String namespaceName, String appId,
                                                                 String executorName,
                                                                 String taskName, PageHelperRequest pageHelperRequest) {
        ExecutorTaskStatisticsHistoryExample historyExample = new ExecutorTaskStatisticsHistoryExample();
        historyExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdEqualTo(appId)
                .andExecutorNameEqualTo(executorName)
                .andTaskNameEqualTo(taskName);
        historyExample.setOrderByClause("id desc");

        int pageSize = pageHelperRequest.getPageSize();
        int pageNum = pageHelperRequest.getPageNum();
        pageSize = (pageSize <= 0) ? DEFAULT_PAGE_SIZE : pageSize;
        PageHelper.startPage(pageNum, pageSize);

        List<ExecutorTaskStatisticsHistory> histories =
                executorTaskStatisticsHistoryMapper.selectByExample(historyExample);
        PageInfo<ExecutorTaskStatisticsHistory> pageInfo = new PageInfo<>(histories);

        return PageResult.<ExecutorTaskStatisticsHistory>builder()
                .content(pageInfo.getList())
                .total(pageInfo.getTotal())
                .pageNum(pageInfo.getPageNum())
                .pageSize(pageInfo.getPageSize())
                .build();
    }

    @Scheduled(fixedDelay = 1000 * REFRESH_TASK_STATISTICS_CYCLE_SECONDS)
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor =
            Exception.class)
    public void refreshTaskStatistics() {
        // 悲观锁锁当天刷新任务
        TaskLock taskLock = taskLockService.selectByTaskDateAndTaskName(LocalDate.now(), REFRESH_TASK_STATISTICS_NAME);
        if (taskLock == null ||
                taskLock.getDataChangeLastModifiedTime().isAfter(LocalDateTime.now().minusSeconds(REFRESH_TASK_STATISTICS_CYCLE_SECONDS))) {
            return;
        }

        taskLock = taskLockService.lock(taskLock.getId());
        if (taskLock == null ||
                taskLock.getDataChangeLastModifiedTime().isAfter(LocalDateTime.now().minusSeconds(REFRESH_TASK_STATISTICS_CYCLE_SECONDS))) {
            return;
        }

        // 获取taskNames
        LocalDate currentDate = LocalDate.now();
        List<ExecutorTask> executorTasks = executorTaskService.findTasksDistinct(currentDate.atStartOfDay(),
                currentDate.plusDays(1).atStartOfDay());

        // 计算各taskName统计数并更新
        executorTasks.stream().forEach(i -> {
            log.info("executorTask:{}", i);

            ExecutorTaskStatistics executorTaskStatisticsOrigin = findOne(i.getNamespaceName(), i.getAppId(),
                    i.getExecutorName(), i.getTaskName(), currentDate);

            ExecutorTaskStatistics executorTaskStatistics = executorTaskService.genTaskStatistics(i.getNamespaceName()
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
                executorTaskStatisticsMapper.insertSelective(executorTaskStatistics);
            } else {
                executorTaskStatistics.setId(executorTaskStatisticsOrigin.getId());
                executorTaskStatistics.setDataChangeCreatedTime(executorTaskStatisticsOrigin.getDataChangeCreatedTime());
                executorTaskStatistics.setVersion(executorTaskStatisticsOrigin.getVersion() + 1);
                executorTaskStatistics.setDataChangeLastModifiedTime(currentDateTime);
                executorTaskStatisticsMapper.updateByPrimaryKeySelective(executorTaskStatistics);
            }
        });

        // 更新刷新任务
        taskLockService.updateModifiedTimeAndVersion(taskLock);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void moveStatistics2History() {
        LocalDate currentDate = LocalDate.now();
        // 创建D日刷新任务
        if (taskLockService.insertTaskLock(currentDate, REFRESH_TASK_STATISTICS_NAME) == 0) {
            return;
        }

        // D-30统计数据复制到历史表

        // 删除D-30数据
    }

}
