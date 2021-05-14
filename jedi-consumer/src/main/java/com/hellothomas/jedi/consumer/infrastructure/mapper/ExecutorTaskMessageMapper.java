package com.hellothomas.jedi.consumer.infrastructure.mapper;

import com.hellothomas.jedi.consumer.domain.ExecutorTaskMessage;
import com.hellothomas.jedi.consumer.domain.ExecutorTaskMessageExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExecutorTaskMessageMapper {
    long countByExample(ExecutorTaskMessageExample example);

    int deleteByExample(ExecutorTaskMessageExample example);

    int deleteByPrimaryKey(String id);

    int insert(ExecutorTaskMessage record);

    int insertSelective(ExecutorTaskMessage record);

    List<ExecutorTaskMessage> selectByExample(ExecutorTaskMessageExample example);

    ExecutorTaskMessage selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") ExecutorTaskMessage record,
                                 @Param("example") ExecutorTaskMessageExample example);

    int updateByExample(@Param("record") ExecutorTaskMessage record,
                        @Param("example") ExecutorTaskMessageExample example);

    int updateByPrimaryKeySelective(ExecutorTaskMessage record);

    int updateByPrimaryKey(ExecutorTaskMessage record);
}