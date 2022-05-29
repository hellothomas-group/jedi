package xyz.hellothomas.jedi.collector.infrastructure.mapper;

import xyz.hellothomas.jedi.collector.domain.ExecutorShutdownMessage;
import xyz.hellothomas.jedi.collector.domain.ExecutorShutdownMessageExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExecutorShutdownMessageMapper {
    long countByExample(ExecutorShutdownMessageExample example);

    int deleteByExample(ExecutorShutdownMessageExample example);

    int deleteByPrimaryKey(String id);

    int insert(ExecutorShutdownMessage record);

    int insertSelective(ExecutorShutdownMessage record);

    List<ExecutorShutdownMessage> selectByExample(ExecutorShutdownMessageExample example);

    ExecutorShutdownMessage selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") ExecutorShutdownMessage record,
                                 @Param("example") ExecutorShutdownMessageExample example);

    int updateByExample(@Param("record") ExecutorShutdownMessage record,
                        @Param("example") ExecutorShutdownMessageExample example);

    int updateByPrimaryKeySelective(ExecutorShutdownMessage record);

    int updateByPrimaryKey(ExecutorShutdownMessage record);
}