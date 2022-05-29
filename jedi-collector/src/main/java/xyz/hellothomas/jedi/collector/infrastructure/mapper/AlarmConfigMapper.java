package xyz.hellothomas.jedi.collector.infrastructure.mapper;

import org.apache.ibatis.annotations.Param;
import xyz.hellothomas.jedi.biz.domain.monitor.AlarmConfig;
import xyz.hellothomas.jedi.biz.domain.monitor.AlarmConfigExample;

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
