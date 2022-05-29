package xyz.hellothomas.jedi.collector.infrastructure.mapper;

import org.apache.ibatis.annotations.Param;
import xyz.hellothomas.jedi.collector.domain.ExecutorTaskStatisticsHistory;
import xyz.hellothomas.jedi.collector.domain.ExecutorTaskStatisticsHistoryExample;

import java.util.List;

public interface ExecutorTaskStatisticsHistoryMapper {
    long countByExample(ExecutorTaskStatisticsHistoryExample example);

    int deleteByExample(ExecutorTaskStatisticsHistoryExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ExecutorTaskStatisticsHistory record);

    int insertSelective(ExecutorTaskStatisticsHistory record);

    List<ExecutorTaskStatisticsHistory> selectByExample(ExecutorTaskStatisticsHistoryExample example);

    ExecutorTaskStatisticsHistory selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ExecutorTaskStatisticsHistory record,
                                 @Param("example") ExecutorTaskStatisticsHistoryExample example);

    int updateByExample(@Param("record") ExecutorTaskStatisticsHistory record,
                        @Param("example") ExecutorTaskStatisticsHistoryExample example);

    int updateByPrimaryKeySelective(ExecutorTaskStatisticsHistory record);

    int updateByPrimaryKey(ExecutorTaskStatisticsHistory record);
}