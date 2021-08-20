package xyz.hellothomas.jedi.consumer.application;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.hellothomas.jedi.consumer.api.dto.PageHelperRequest;
import xyz.hellothomas.jedi.consumer.api.dto.PageResult;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskStatistics;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskStatisticsExample;
import xyz.hellothomas.jedi.consumer.infrastructure.mapper.ExecutorTaskStatisticsMapper;

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
public class ExecutorTaskStatisticsService {
    private final ExecutorTaskStatisticsMapper executorTaskStatisticsMapper;

    public ExecutorTaskStatisticsService(ExecutorTaskStatisticsMapper executorTaskStatisticsMapper) {
        this.executorTaskStatisticsMapper = executorTaskStatisticsMapper;
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

    public int updateByPrimaryKeySelective(ExecutorTaskStatistics record) {
        return executorTaskStatisticsMapper.updateByPrimaryKeySelective(record);
    }

    public int insertSelective(ExecutorTaskStatistics record) {
        return executorTaskStatisticsMapper.insertSelective(record);
    }

    public int deleteByPrimaryKey(Integer id) {
        return executorTaskStatisticsMapper.deleteByPrimaryKey(id);
    }
}
