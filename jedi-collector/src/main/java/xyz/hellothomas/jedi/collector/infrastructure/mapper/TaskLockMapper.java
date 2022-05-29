package xyz.hellothomas.jedi.collector.infrastructure.mapper;

import org.apache.ibatis.annotations.Param;
import xyz.hellothomas.jedi.collector.domain.TaskLock;
import xyz.hellothomas.jedi.collector.domain.TaskLockExample;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TaskLockMapper {
    long countByExample(TaskLockExample example);

    int deleteByExample(TaskLockExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TaskLock record);

    int insertSelective(TaskLock record);

    List<TaskLock> selectByExample(TaskLockExample example);

    TaskLock selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TaskLock record, @Param("example") TaskLockExample example);

    int updateByExample(@Param("record") TaskLock record, @Param("example") TaskLockExample example);

    int updateByPrimaryKeySelective(TaskLock record);

    int updateByPrimaryKey(TaskLock record);

    TaskLock selectByUniqueKey(@Param("taskDate") LocalDate taskDate, @Param("taskName") String taskName);

    TaskLock selectByPrimaryKeyForUpdate(Integer id);

    int updateLockByUniqueKey(@Param("taskDate") LocalDate taskDate, @Param("taskName") String taskName,
                              @Param("isLocked") Boolean isLocked,
                              @Param("updateTime") LocalDateTime updateTime);
}
