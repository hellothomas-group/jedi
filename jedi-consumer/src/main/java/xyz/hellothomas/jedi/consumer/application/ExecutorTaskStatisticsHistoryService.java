package xyz.hellothomas.jedi.consumer.application;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.hellothomas.jedi.consumer.api.dto.PageHelperRequest;
import xyz.hellothomas.jedi.consumer.api.dto.PageResult;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskStatisticsHistory;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskStatisticsHistoryExample;
import xyz.hellothomas.jedi.consumer.infrastructure.mapper.ExecutorTaskStatisticsHistoryMapper;

import java.time.LocalDate;
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
    private final ExecutorTaskStatisticsHistoryMapper executorTaskStatisticsHistoryMapper;

    public ExecutorTaskStatisticsHistoryService(ExecutorTaskStatisticsHistoryMapper executorTaskStatisticsHistoryMapper) {
        this.executorTaskStatisticsHistoryMapper = executorTaskStatisticsHistoryMapper;
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
                                                              LocalDate startDate,
                                                              LocalDate endDate, String taskName,
                                                              PageHelperRequest pageHelperRequest) {
        ExecutorTaskStatisticsHistoryExample historyExample = new ExecutorTaskStatisticsHistoryExample();

        ExecutorTaskStatisticsHistoryExample.Criteria criteria =
                historyExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                        .andAppIdEqualTo(appId)
                        .andExecutorNameEqualTo(executorName);
        if (taskName != null) {
            criteria.andTaskNameEqualTo(taskName);
        }
        criteria.andStatisticsDateBetween(startDate, endDate);
        historyExample.setOrderByClause("statistics_date, task_name");

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

    public int updateByPrimaryKeySelective(ExecutorTaskStatisticsHistory record) {
        return executorTaskStatisticsHistoryMapper.updateByPrimaryKeySelective(record);
    }
}
