package xyz.hellothomas.jedi.consumer.application;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xyz.hellothomas.jedi.consumer.api.dto.PageHelperRequest;
import xyz.hellothomas.jedi.consumer.api.dto.PageResult;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskStatistics;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskStatisticsExample;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskStatisticsHistory;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskStatisticsHistoryExample;
import xyz.hellothomas.jedi.consumer.domain.pojo.ExecutorTask;
import xyz.hellothomas.jedi.consumer.infrastructure.mapper.ExecutorTaskStatisticsHistoryMapper;
import xyz.hellothomas.jedi.consumer.infrastructure.mapper.ExecutorTaskStatisticsMapper;
import xyz.hellothomas.jedi.core.utils.SleepUtil;

import java.math.BigDecimal;
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

    public ExecutorTaskStatistics findCurrentOne(String namespaceName, String appId, String executorName,
                                                 String taskName) {
        ExecutorTaskStatisticsExample executorTaskStatisticsExample = new ExecutorTaskStatisticsExample();
        executorTaskStatisticsExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdEqualTo(appId)
                .andExecutorNameEqualTo(executorName)
                .andTaskNameEqualTo(taskName);

        List<ExecutorTaskStatistics> executorTaskStatisticsList =
                executorTaskStatisticsMapper.selectByExample(executorTaskStatisticsExample);

        return executorTaskStatisticsList.isEmpty() ? null : executorTaskStatisticsList.get(0);
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

    @Scheduled(fixedDelay = 1000 * 60 * 2)
    public void refreshTaskStatistics() {
        // 乐观锁锁当天刷新任务
        if (taskLockService.lock(LocalDate.now(), REFRESH_TASK_STATISTICS_NAME) == 0) {
            return;
        }

        // 获取taskNames
        LocalDate currentDate = LocalDate.now();
        List<ExecutorTask> executorTasks = executorTaskService.findTasksDistinct(currentDate.atStartOfDay(),
                currentDate.plusDays(1).atStartOfDay());

        // 计算各taskName统计数并更新
        executorTasks.stream().forEach(i -> {
            log.info("executorTask:{}", i);

            ExecutorTaskStatistics executorTaskStatisticsOrigin = findCurrentOne(i.getNamespaceName(), i.getAppId(),
                    i.getExecutorName(), i.getTaskName());

            ExecutorTaskStatistics executorTaskStatistics = executorTaskService.genTaskStatistics(i.getNamespaceName()
                    , i.getAppId(),
                    i.getExecutorName(),
                    i.getTaskName(), currentDate.atStartOfDay(), currentDate.plusDays(1).atStartOfDay());
            log.info("executorTaskStatistics:{}", executorTaskStatistics);
            if (executorTaskStatistics.getTotal() == 0) {
                executorTaskStatistics.setFailureRatio(new BigDecimal(0));
            } else {
                executorTaskStatistics.setFailureRatio(new BigDecimal(executorTaskStatistics.getFailure()).divide(new BigDecimal(executorTaskStatistics.getTotal())));
            }

            LocalDateTime currentDateTime = LocalDateTime.now();
            if (executorTaskStatisticsOrigin == null) {
                executorTaskStatistics.setDataChangeCreatedTime(currentDateTime);
                executorTaskStatistics.setDataChangeLastModifiedTime(currentDateTime);
                executorTaskStatistics.setVersion(1);
                executorTaskStatisticsMapper.insertSelective(executorTaskStatistics);
            } else {
                executorTaskStatistics.setDataChangeLastModifiedTime(currentDateTime);
                executorTaskStatistics.setVersion(executorTaskStatistics.getVersion() + 1);
                executorTaskStatisticsMapper.updateByPrimaryKeySelective(executorTaskStatistics);
            }
        });

        // 释放乐观锁
        taskLockService.unlock(currentDate, REFRESH_TASK_STATISTICS_NAME);
    }

    @Scheduled(cron = "0 20 19 * * ?")
    public void moveStatistics2History() {
        LocalDate currentDate = LocalDate.now();
        // 创建D日刷新任务并乐观锁锁住
        if (taskLockService.insertLockedOne(currentDate, REFRESH_TASK_STATISTICS_NAME) == 0) {
            return;
        }

        // D-1统计数据复制到历史表

        // 删除D-1数据

        // 删除D-1刷新任务锁

        // 释放乐观锁
        SleepUtil.sleep(10000);

        taskLockService.unlock(currentDate, REFRESH_TASK_STATISTICS_NAME);
    }

}
