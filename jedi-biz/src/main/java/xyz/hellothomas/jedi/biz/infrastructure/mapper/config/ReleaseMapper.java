package xyz.hellothomas.jedi.biz.infrastructure.mapper.config;

import org.apache.ibatis.annotations.Param;
import xyz.hellothomas.jedi.biz.domain.config.Release;
import xyz.hellothomas.jedi.biz.domain.config.ReleaseExample;

import java.util.List;

public interface ReleaseMapper {
    long countByExample(ReleaseExample example);

    int deleteByExample(ReleaseExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Release record);

    int insertSelective(Release record);

    List<Release> selectByExample(ReleaseExample example);

    Release selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Release record, @Param("example") ReleaseExample example);

    int updateByExample(@Param("record") Release record, @Param("example") ReleaseExample example);

    int updateByPrimaryKeySelective(Release record);

    int updateByPrimaryKey(Release record);

    Release findFirstByNamespaceNameAndAppIdAndExecutorNameAndIsAbandonedFalseOrderByIdDesc(@Param("namespaceName") String namespaceName,
                                                                                            @Param("appId") String appId,
                                                                                            @Param("executorName") String executorName);
}
