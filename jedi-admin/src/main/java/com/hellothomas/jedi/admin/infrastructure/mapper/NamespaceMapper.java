package com.hellothomas.jedi.admin.infrastructure.mapper;

import com.hellothomas.jedi.admin.domain.Namespace;
import com.hellothomas.jedi.admin.domain.NamespaceExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NamespaceMapper {
    long countByExample(NamespaceExample example);

    int deleteByExample(NamespaceExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Namespace record);

    int insertSelective(Namespace record);

    List<Namespace> selectByExample(NamespaceExample example);

    Namespace selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Namespace record, @Param("example") NamespaceExample example);

    int updateByExample(@Param("record") Namespace record, @Param("example") NamespaceExample example);

    int updateByPrimaryKeySelective(Namespace record);

    int updateByPrimaryKey(Namespace record);
}