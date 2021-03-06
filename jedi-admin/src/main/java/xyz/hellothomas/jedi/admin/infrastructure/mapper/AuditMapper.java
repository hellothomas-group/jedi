package xyz.hellothomas.jedi.admin.infrastructure.mapper;

import org.apache.ibatis.annotations.Param;
import xyz.hellothomas.jedi.admin.domain.Audit;
import xyz.hellothomas.jedi.admin.domain.AuditExample;

import java.util.List;

public interface AuditMapper {
    long countByExample(AuditExample example);

    int deleteByExample(AuditExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Audit record);

    int insertSelective(Audit record);

    List<Audit> selectByExample(AuditExample example);

    Audit selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Audit record, @Param("example") AuditExample example);

    int updateByExample(@Param("record") Audit record, @Param("example") AuditExample example);

    int updateByPrimaryKeySelective(Audit record);

    int updateByPrimaryKey(Audit record);
}