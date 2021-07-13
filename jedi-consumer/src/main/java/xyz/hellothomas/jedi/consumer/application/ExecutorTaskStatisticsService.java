package xyz.hellothomas.jedi.consumer.application;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.hellothomas.jedi.consumer.api.dto.PageHelperRequest;
import xyz.hellothomas.jedi.consumer.api.dto.PageResult;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskStatistics;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskStatisticsExample;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskStatisticsHistory;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskStatisticsHistoryExample;
import xyz.hellothomas.jedi.consumer.infrastructure.mapper.ExecutorTaskStatisticsHistoryMapper;
import xyz.hellothomas.jedi.consumer.infrastructure.mapper.ExecutorTaskStatisticsMapper;

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
    private final ExecutorTaskStatisticsMapper executorTaskStatisticsMapper;
    private final ExecutorTaskStatisticsHistoryMapper executorTaskStatisticsHistoryMapper;

    public ExecutorTaskStatisticsService(ExecutorTaskStatisticsMapper executorTaskStatisticsMapper,
                                         ExecutorTaskStatisticsHistoryMapper executorTaskStatisticsHistoryMapper) {
        this.executorTaskStatisticsMapper = executorTaskStatisticsMapper;
        this.executorTaskStatisticsHistoryMapper = executorTaskStatisticsHistoryMapper;
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

    public void moveStatistics2History() {

    }

}
