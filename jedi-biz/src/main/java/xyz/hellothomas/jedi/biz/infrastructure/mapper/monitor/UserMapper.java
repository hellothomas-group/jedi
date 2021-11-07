package xyz.hellothomas.jedi.biz.infrastructure.mapper.monitor;

import org.apache.ibatis.annotations.Param;
import xyz.hellothomas.jedi.biz.domain.monitor.User;
import xyz.hellothomas.jedi.biz.domain.monitor.UserExample;

import java.util.List;

public interface UserMapper {
    long countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByUserName(@Param("userName") String userName);
}
