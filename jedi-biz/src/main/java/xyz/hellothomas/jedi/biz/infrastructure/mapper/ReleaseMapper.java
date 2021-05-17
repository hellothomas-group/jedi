package xyz.hellothomas.jedi.biz.infrastructure.mapper;

import xyz.hellothomas.jedi.biz.domain.Release;
import xyz.hellothomas.jedi.biz.domain.ReleaseExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReleaseMapper {
    long countByExample(ReleaseExample example);

    int deleteByExample(ReleaseExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Release record);

    int insertSelective(Release record);

    List<Release> selectByExampleWithBLOBs(ReleaseExample example);

    List<Release> selectByExample(ReleaseExample example);

    Release selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Release record, @Param("example") ReleaseExample example);

    int updateByExampleWithBLOBs(@Param("record") Release record, @Param("example") ReleaseExample example);

    int updateByExample(@Param("record") Release record, @Param("example") ReleaseExample example);

    int updateByPrimaryKeySelective(Release record);

    int updateByPrimaryKeyWithBLOBs(Release record);

    int updateByPrimaryKey(Release record);

    Release findFirstByNamespaceNameAndAppIdAndExecutorNameAndIsAbandonedFalseOrderByIdDesc(@Param("namespaceName") String namespaceName,
                                                                                            @Param("appId") String appId,
                                                                                            @Param("executorName") String executorName);
}