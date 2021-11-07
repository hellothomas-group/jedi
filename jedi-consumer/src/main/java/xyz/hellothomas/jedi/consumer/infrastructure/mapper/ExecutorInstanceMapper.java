package xyz.hellothomas.jedi.consumer.infrastructure.mapper;

import org.apache.ibatis.annotations.Param;
import xyz.hellothomas.jedi.consumer.domain.ExecutorInstance;
import xyz.hellothomas.jedi.consumer.domain.ExecutorInstanceExample;

import java.util.List;

public interface ExecutorInstanceMapper {
    long countByExample(ExecutorInstanceExample example);

    int deleteByExample(ExecutorInstanceExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ExecutorInstance record);

    int insertSelective(ExecutorInstance record);

    List<ExecutorInstance> selectByExample(ExecutorInstanceExample example);

    ExecutorInstance selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ExecutorInstance record,
                                 @Param("example") ExecutorInstanceExample example);

    int updateByExample(@Param("record") ExecutorInstance record, @Param("example") ExecutorInstanceExample example);

    int updateByPrimaryKeySelective(ExecutorInstance record);

    int updateByPrimaryKey(ExecutorInstance record);

    int insertOrUpdate(@Param("record") ExecutorInstance record);
}