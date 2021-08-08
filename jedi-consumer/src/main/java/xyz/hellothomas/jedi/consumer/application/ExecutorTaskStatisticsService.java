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
import xyz.hellothomas.jedi.consumer.common.util.LocalBeanUtils;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskStatistics;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskStatisticsExample;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskStatisticsHistory;
import xyz.hellothomas.jedi.consumer.domain.TaskLock;
import xyz.hellothomas.jedi.consumer.domain.pojo.ExecutorTask;
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
    private final ExecutorTaskService executorTaskService;
    private final TaskLockService taskLockService;
    private final ExecutorTaskStatisticsHistoryService executorTaskStatisticsHistoryService;

    public ExecutorTaskStatisticsService(ExecutorTaskStatisticsMapper executorTaskStatisticsMapper,
                                         ExecutorTaskService executorTaskService, TaskLockService taskLockService,
                                         ExecutorTaskStatisticsHistoryService executorTaskStatisticsHistoryService) {
        this.executorTaskStatisticsMapper = executorTaskStatisticsMapper;
        this.executorTaskService = executorTaskService;
        this.taskLockService = taskLockService;
        this.executorTaskStatisticsHistoryService = executorTaskStatisticsHistoryService;
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

    /**
     * 不包括endStatisticsDate
     * @param endStatisticsDate
     * @param pageHelperRequest
     * @return
     */
    public PageResult<ExecutorTaskStatistics> findList(LocalDate endStatisticsDate,
                                                       PageHelperRequest pageHelperRequest) {
        ExecutorTaskStatisticsExample executorTaskStatisticsExample = new ExecutorTaskStatisticsExample();
        executorTaskStatisticsExample.createCriteria().andStatisticsDateLessThan(endStatisticsDate);

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
        taskLock = taskLockService.lock(taskLock.getId());
        if (taskLock == null ||
                taskLock.getDataChangeLastModifiedTime().isAfter(LocalDateTime.now().minusSeconds(REFRESH_TASK_STATISTICS_CYCLE_SECONDS))) {
            return;
        }

        // 获取taskNames
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

    /**
     * D日前数据移至历史表
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor =
            Exception.class)
    public void moveStatistics2History() {
        LocalDate currentDate = LocalDate.now();
        // 创建D日刷新任务
        if (taskLockService.insertTaskLock(currentDate, REFRESH_TASK_STATISTICS_NAME) == 0) {
            return;
        }

        // D日前统计数据复制到历史表
        PageHelperRequest pageHelperRequest = new PageHelperRequest();
        pageHelperRequest.setPageNum(1);
        pageHelperRequest.setPageSize(1000);
        while (true) {
            PageResult<ExecutorTaskStatistics> pageResult = findList(currentDate, pageHelperRequest);
            pageResult.getContent().stream().forEach(i -> {
                ExecutorTaskStatisticsHistory executorTaskStatisticsHistory =
                        LocalBeanUtils.transform(ExecutorTaskStatisticsHistory.class, i);
                // 复制到历史表
                executorTaskStatisticsHistoryService.insertOne(executorTaskStatisticsHistory);
                // 删除数据
                executorTaskStatisticsMapper.deleteByPrimaryKey(i.getId());
            });

            if (pageResult.getPageNum() == pageResult.getTotal()) {
                break;
            } else {
                pageHelperRequest.setPageNum(pageResult.getPageNum() + 1);
            }
        }
    }
}
