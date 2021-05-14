package com.hellothomas.jedi.admin.infrastructure.mapper;

import com.hellothomas.jedi.admin.domain.ReleaseHistory;
import com.hellothomas.jedi.admin.domain.ReleaseHistoryExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReleaseHistoryMapper {
    long countByExample(ReleaseHistoryExample example);

    int deleteByExample(ReleaseHistoryExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ReleaseHistory record);

    int insertSelective(ReleaseHistory record);

    List<ReleaseHistory> selectByExampleWithBLOBs(ReleaseHistoryExample example);

    List<ReleaseHistory> selectByExample(ReleaseHistoryExample example);

    ReleaseHistory selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ReleaseHistory record,
                                 @Param("example") ReleaseHistoryExample example);

    int updateByExampleWithBLOBs(@Param("record") ReleaseHistory record,
                                 @Param("example") ReleaseHistoryExample example);

    int updateByExample(@Param("record") ReleaseHistory record, @Param("example") ReleaseHistoryExample example);

    int updateByPrimaryKeySelective(ReleaseHistory record);

    int updateByPrimaryKeyWithBLOBs(ReleaseHistory record);

    int updateByPrimaryKey(ReleaseHistory record);
}