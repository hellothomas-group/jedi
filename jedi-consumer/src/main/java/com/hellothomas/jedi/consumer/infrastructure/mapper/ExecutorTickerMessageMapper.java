package com.hellothomas.jedi.consumer.infrastructure.mapper;

import com.hellothomas.jedi.consumer.domain.ExecutorTickerMessage;
import com.hellothomas.jedi.consumer.domain.ExecutorTickerMessageExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExecutorTickerMessageMapper {
    long countByExample(ExecutorTickerMessageExample example);

    int deleteByExample(ExecutorTickerMessageExample example);

    int deleteByPrimaryKey(String id);

    int insert(ExecutorTickerMessage record);

    int insertSelective(ExecutorTickerMessage record);

    List<ExecutorTickerMessage> selectByExample(ExecutorTickerMessageExample example);

    ExecutorTickerMessage selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") ExecutorTickerMessage record,
                                 @Param("example") ExecutorTickerMessageExample example);

    int updateByExample(@Param("record") ExecutorTickerMessage record,
                        @Param("example") ExecutorTickerMessageExample example);

    int updateByPrimaryKeySelective(ExecutorTickerMessage record);

    int updateByPrimaryKey(ExecutorTickerMessage record);
}