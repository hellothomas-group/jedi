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
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskStatisticsHistory;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskStatisticsHistoryExample;
import xyz.hellothomas.jedi.consumer.domain.TaskLock;
import xyz.hellothomas.jedi.consumer.domain.pojo.ExecutorTask;
import xyz.hellothomas.jedi.consumer.infrastructure.mapper.ExecutorTaskStatisticsHistoryMapper;

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
public class ExecutorTaskStatisticsHistoryService {
    private static final String REFRESH_LAST_DAY_TASK_STATISTICS_NAME = "REFRESH_LAST_DAY_TASK_STATISTICS";
    private final ExecutorTaskStatisticsHistoryMapper executorTaskStatisticsHistoryMapper;
    private final ExecutorTaskService executorTaskService;
    private final TaskLockService taskLockService;

    public ExecutorTaskStatisticsHistoryService(ExecutorTaskStatisticsHistoryMapper executorTaskStatisticsHistoryMapper,
                                                ExecutorTaskService executorTaskService,
                                                TaskLockService taskLockService) {
        this.executorTaskStatisticsHistoryMapper = executorTaskStatisticsHistoryMapper;
        this.executorTaskService = executorTaskService;
        this.taskLockService = taskLockService;
    }

    public ExecutorTaskStatisticsHistory findOne(String namespaceName, String appId, String executorName,
                                                 String taskName,
                                                 LocalDate statisticsDate) {
        ExecutorTaskStatisticsHistoryExample executorTaskStatisticsHistoryExample =
                new ExecutorTaskStatisticsHistoryExample();
        executorTaskStatisticsHistoryExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdEqualTo(appId)
                .andExecutorNameEqualTo(executorName)
                .andTaskNameEqualTo(taskName)
                .andStatisticsDateEqualTo(statisticsDate);

        List<ExecutorTaskStatisticsHistory> executorTaskStatisticsHistoryList =
                executorTaskStatisticsHistoryMapper.selectByExample(executorTaskStatisticsHistoryExample);

        return executorTaskStatisticsHistoryList.isEmpty() ? null : executorTaskStatisticsHistoryList.get(0);
    }

    public PageResult<ExecutorTaskStatisticsHistory> findList(String namespaceName, String appId,
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

    public int insertOne(ExecutorTaskStatisticsHistory executorTaskStatisticsHistory) {
        return executorTaskStatisticsHistoryMapper.insert(executorTaskStatisticsHistory);
    }

    /**
     * 刷新统计D-1日数据
     */
    @Scheduled(cron = "0 0 1,3,5,9,17,23 * * ?")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor =
            Exception.class)
    public void refreshLastDayTaskStatistics() {
        LocalDate lastDate = LocalDate.now().minusDays(1);
        TaskLock taskLock = taskLockService.selectByTaskDateAndTaskName(lastDate,
                REFRESH_LAST_DAY_TASK_STATISTICS_NAME);
        if (taskLock == null) {
            return;
        }

        // 悲观锁锁D-1刷新任务
        taskLock = taskLockService.lock(taskLock.getId());
        if (taskLock == null) {
            return;
        }

        // 获取taskNames
        List<ExecutorTask> executorTasks = executorTaskService.findTasksDistinct(lastDate.atStartOfDay(),
                lastDate.plusDays(1).atStartOfDay());

        // 计算各taskName统计数并更新
        executorTasks.stream().forEach(i -> {
            log.info("executorTask:{}", i);

            ExecutorTaskStatisticsHistory executorTaskStatisticsHistoryOrigin = findOne(i.getNamespaceName(),
                    i.getAppId(),
                    i.getExecutorName(), i.getTaskName(), lastDate);

            if (executorTaskStatisticsHistoryOrigin == null) {
                return;
            }

            ExecutorTaskStatistics executorTaskStatistics = executorTaskService.genTaskStatistics(i.getNamespaceName()
                    , i.getAppId(),
                    i.getExecutorName(),
                    i.getTaskName(), lastDate.atStartOfDay(), lastDate.plusDays(1).atStartOfDay());
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
            executorTaskStatisticsHistoryMapper.updateByPrimaryKeySelective(executorTaskStatisticsHistory);
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
    }
}
