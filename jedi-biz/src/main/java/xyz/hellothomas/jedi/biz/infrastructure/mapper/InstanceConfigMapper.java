package xyz.hellothomas.jedi.biz.infrastructure.mapper;

import xyz.hellothomas.jedi.biz.domain.InstanceConfig;
import xyz.hellothomas.jedi.biz.domain.InstanceConfigExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InstanceConfigMapper {
    long countByExample(InstanceConfigExample example);

    int deleteByExample(InstanceConfigExample example);

    int deleteByPrimaryKey(Long id);

    int insert(InstanceConfig record);

    int insertSelective(InstanceConfig record);

    List<InstanceConfig> selectByExample(InstanceConfigExample example);

    InstanceConfig selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") InstanceConfig record,
                                 @Param("example") InstanceConfigExample example);

    int updateByExample(@Param("record") InstanceConfig record, @Param("example") InstanceConfigExample example);

    int updateByPrimaryKeySelective(InstanceConfig record);

    int updateByPrimaryKey(InstanceConfig record);
}