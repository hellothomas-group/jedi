package xyz.hellothomas.jedi.biz.infrastructure.mapper;

import xyz.hellothomas.jedi.biz.domain.Instance;
import xyz.hellothomas.jedi.biz.domain.InstanceExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InstanceMapper {
    long countByExample(InstanceExample example);

    int deleteByExample(InstanceExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Instance record);

    int insertSelective(Instance record);

    List<Instance> selectByExample(InstanceExample example);

    Instance selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Instance record, @Param("example") InstanceExample example);

    int updateByExample(@Param("record") Instance record, @Param("example") InstanceExample example);

    int updateByPrimaryKeySelective(Instance record);

    int updateByPrimaryKey(Instance record);
}