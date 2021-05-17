package xyz.hellothomas.jedi.biz.infrastructure.mapper;

import xyz.hellothomas.jedi.biz.domain.ReleaseMessage;
import xyz.hellothomas.jedi.biz.domain.ReleaseMessageExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReleaseMessageMapper {
    long countByExample(ReleaseMessageExample example);

    int deleteByExample(ReleaseMessageExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ReleaseMessage record);

    int insertSelective(ReleaseMessage record);

    List<ReleaseMessage> selectByExample(ReleaseMessageExample example);

    ReleaseMessage selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ReleaseMessage record,
                                 @Param("example") ReleaseMessageExample example);

    int updateByExample(@Param("record") ReleaseMessage record, @Param("example") ReleaseMessageExample example);

    int updateByPrimaryKeySelective(ReleaseMessage record);

    int updateByPrimaryKey(ReleaseMessage record);

    List<ReleaseMessage> findFirst500ByIdGreaterThanOrderByIdAsc(@Param("id") Long id);

    ReleaseMessage findTopByOrderByIdDesc();

    /**
     * id自增
     * @param record
     * @return
     */
    int save(ReleaseMessage record);

    List<ReleaseMessage> findFirst100ByMessageAndIdLessThanOrderByIdAsc(@Param("message") String message,
                                                                        @Param("id") Long id);

    void deleteAll(@Param("records") List<ReleaseMessage> records);
}