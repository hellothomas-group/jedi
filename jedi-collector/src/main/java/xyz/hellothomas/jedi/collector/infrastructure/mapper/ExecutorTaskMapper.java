package xyz.hellothomas.jedi.collector.infrastructure.mapper;

import org.apache.ibatis.annotations.Param;
import xyz.hellothomas.jedi.collector.domain.ExecutorTask;
import xyz.hellothomas.jedi.collector.domain.ExecutorTaskExample;

import java.util.List;

public interface ExecutorTaskMapper {
    long countByExample(ExecutorTaskExample example);

    int deleteByExample(ExecutorTaskExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ExecutorTask record);

    int insertSelective(ExecutorTask record);

    List<ExecutorTask> selectByExample(ExecutorTaskExample example);

    ExecutorTask selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ExecutorTask record, @Param("example") ExecutorTaskExample example);

    int updateByExample(@Param("record") ExecutorTask record, @Param("example") ExecutorTaskExample example);

    int updateByPrimaryKeySelective(ExecutorTask record);

    int updateByPrimaryKey(ExecutorTask record);

    int insertOrUpdate(@Param("record") ExecutorTask record);
}