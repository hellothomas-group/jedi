package xyz.hellothomas.jedi.admin.application;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import xyz.hellothomas.jedi.admin.infrastructure.listener.SyncEvent;
import xyz.hellothomas.jedi.biz.common.enums.SyncOperationEnum;
import xyz.hellothomas.jedi.biz.common.enums.SyncTypeEnum;
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
    private final ApplicationEventPublisher applicationEventPublisher;

    public UserService(UserMapper userMapper, ApplicationEventPublisher applicationEventPublisher) {
        this.userMapper = userMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public User getUserByUserName(String userName) {
        return userMapper.selectByUserName(userName);
    }

    public User saveUser(User user) {
        userMapper.insertSelective(user);
        applicationEventPublisher.publishEvent(new SyncEvent(user, SyncTypeEnum.USER, SyncOperationEnum.CREATION));

        return user;
    }

    public User getUserById(Long userId) {
        return userMapper.selectByPrimaryKey(userId);
    }
}
