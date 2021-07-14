package xyz.hellothomas.jedi.consumer.infrastructure.mapper;

import org.apache.ibatis.annotations.Param;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskMessage;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskMessageExample;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskStatistics;
import xyz.hellothomas.jedi.consumer.domain.pojo.ExecutorTask;

import java.time.LocalDateTime;
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

    List<ExecutorTask> selectByRecordTimeAndGroupByTask(@Param("startTime") LocalDateTime startTime,
                                                        @Param("endTime") LocalDateTime endTime);

    ExecutorTaskStatistics selectStatisticsByUniqueKeyAndRecordTime(@Param("namespaceName") String namespaceName,
                                                                    @Param("appId") String appId,
                                                                    @Param("executorName") String executorName,
                                                                    @Param("taskName") String taskName,
                                                                    @Param("startTime") LocalDateTime startTime,
                                                                    @Param("endTime") LocalDateTime endTime);
}
