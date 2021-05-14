package com.hellothomas.jedi.admin.infrastructure.mapper;

import com.hellothomas.jedi.admin.domain.Executor;
import com.hellothomas.jedi.admin.domain.ExecutorExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExecutorMapper {
    long countByExample(ExecutorExample example);

    int deleteByExample(ExecutorExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Executor record);

    int insertSelective(Executor record);

    List<Executor> selectByExample(ExecutorExample example);

    Executor selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Executor record, @Param("example") ExecutorExample example);

    int updateByExample(@Param("record") Executor record, @Param("example") ExecutorExample example);

    int updateByPrimaryKeySelective(Executor record);

    int updateByPrimaryKey(Executor record);
}