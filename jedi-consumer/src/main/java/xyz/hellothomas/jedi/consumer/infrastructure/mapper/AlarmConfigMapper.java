package xyz.hellothomas.jedi.consumer.infrastructure.mapper;

import org.apache.ibatis.annotations.Param;
import xyz.hellothomas.jedi.consumer.domain.AlarmConfig;
import xyz.hellothomas.jedi.consumer.domain.AlarmConfigExample;

import java.util.List;

public interface AlarmConfigMapper {
    long countByExample(AlarmConfigExample example);

    int deleteByExample(AlarmConfigExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AlarmConfig record);

    int insertSelective(AlarmConfig record);

    List<AlarmConfig> selectByExample(AlarmConfigExample example);

    AlarmConfig selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AlarmConfig record, @Param("example") AlarmConfigExample example);

    int updateByExample(@Param("record") AlarmConfig record, @Param("example") AlarmConfigExample example);

    int updateByPrimaryKeySelective(AlarmConfig record);

    int updateByPrimaryKey(AlarmConfig record);
}
