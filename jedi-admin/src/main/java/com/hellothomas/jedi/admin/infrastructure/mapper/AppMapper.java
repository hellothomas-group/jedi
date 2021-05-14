package com.hellothomas.jedi.admin.infrastructure.mapper;

import com.hellothomas.jedi.admin.domain.App;
import com.hellothomas.jedi.admin.domain.AppExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppMapper {
    long countByExample(AppExample example);

    int deleteByExample(AppExample example);

    int deleteByPrimaryKey(Long id);

    int insert(App record);

    int insertSelective(App record);

    List<App> selectByExample(AppExample example);

    App selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") App record, @Param("example") AppExample example);

    int updateByExample(@Param("record") App record, @Param("example") AppExample example);

    int updateByPrimaryKeySelective(App record);

    int updateByPrimaryKey(App record);
}