package xyz.hellothomas.jedi.admin.application;

import org.springframework.stereotype.Service;
import xyz.hellothomas.jedi.admin.domain.User;
import xyz.hellothomas.jedi.admin.infrastructure.mapper.UserMapper;

/**
 * @author 80234613 唐圆
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

    public User saveUser(User user) {
        userMapper.insertSelective(user);
        return user;
    }

    public User getUserById(int userId) {
        return userMapper.selectByPrimaryKey(userId);
    }
}
