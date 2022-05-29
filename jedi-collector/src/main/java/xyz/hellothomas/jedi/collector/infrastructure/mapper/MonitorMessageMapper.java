package xyz.hellothomas.jedi.collector.infrastructure.mapper;

import xyz.hellothomas.jedi.collector.domain.MonitorMessage;
import xyz.hellothomas.jedi.collector.domain.MonitorMessageExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MonitorMessageMapper {
    long countByExample(MonitorMessageExample example);

    int deleteByExample(MonitorMessageExample example);

    int deleteByPrimaryKey(String id);

    int insert(MonitorMessage record);

    int insertSelective(MonitorMessage record);

    List<MonitorMessage> selectByExample(MonitorMessageExample example);

    MonitorMessage selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") MonitorMessage record,
                                 @Param("example") MonitorMessageExample example);

    int updateByExample(@Param("record") MonitorMessage record, @Param("example") MonitorMessageExample example);

    int updateByPrimaryKeySelective(MonitorMessage record);

    int updateByPrimaryKey(MonitorMessage record);
}