package xyz.hellothomas.jedi.admin.infrastructure.mapper;

import org.apache.ibatis.annotations.Param;
import xyz.hellothomas.jedi.admin.domain.ExecutorLock;
import xyz.hellothomas.jedi.admin.domain.ExecutorLockExample;

import java.util.List;

public interface ExecutorLockMapper {
    long countByExample(ExecutorLockExample example);

    int deleteByExample(ExecutorLockExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ExecutorLock record);

    int insertSelective(ExecutorLock record);

    List<ExecutorLock> selectByExample(ExecutorLockExample example);

    ExecutorLock selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ExecutorLock record, @Param("example") ExecutorLockExample example);

    int updateByExample(@Param("record") ExecutorLock record, @Param("example") ExecutorLockExample example);

    int updateByPrimaryKeySelective(ExecutorLock record);

    int updateByPrimaryKey(ExecutorLock record);
}