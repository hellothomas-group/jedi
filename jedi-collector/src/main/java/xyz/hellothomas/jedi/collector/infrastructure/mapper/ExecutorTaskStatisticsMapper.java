package xyz.hellothomas.jedi.collector.infrastructure.mapper;

import org.apache.ibatis.annotations.Param;
import xyz.hellothomas.jedi.collector.domain.ExecutorTaskStatistics;
import xyz.hellothomas.jedi.collector.domain.ExecutorTaskStatisticsExample;

import java.util.List;

public interface ExecutorTaskStatisticsMapper {
    long countByExample(ExecutorTaskStatisticsExample example);

    int deleteByExample(ExecutorTaskStatisticsExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ExecutorTaskStatistics record);

    int insertSelective(ExecutorTaskStatistics record);

    List<ExecutorTaskStatistics> selectByExample(ExecutorTaskStatisticsExample example);

    ExecutorTaskStatistics selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ExecutorTaskStatistics record,
                                 @Param("example") ExecutorTaskStatisticsExample example);

    int updateByExample(@Param("record") ExecutorTaskStatistics record,
                        @Param("example") ExecutorTaskStatisticsExample example);

    int updateByPrimaryKeySelective(ExecutorTaskStatistics record);

    int updateByPrimaryKey(ExecutorTaskStatistics record);
}