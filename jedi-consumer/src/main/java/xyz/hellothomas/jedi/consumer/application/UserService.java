package xyz.hellothomas.jedi.consumer.application;

import org.springframework.stereotype.Service;
import xyz.hellothomas.jedi.biz.domain.monitor.User;
import xyz.hellothomas.jedi.biz.infrastructure.mapper.monitor.UserMapper;

/**
 * @author Thomas
 * @date 2021/7/8 10:58
 * @descripton
 * @version 1.0
 */
@Service
public class UserService {
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User getUserByUserName(String userName) {
        return userMapper.selectByUserName(userName);
    }

    public void saveUser(User user) {
        try {
            userMapper.insertSelective(user);
        } catch (Exception e) {
            userMapper.updateByPrimaryKey(user);
        }
    }
}
