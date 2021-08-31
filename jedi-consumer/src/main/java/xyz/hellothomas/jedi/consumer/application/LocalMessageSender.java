package xyz.hellothomas.jedi.consumer.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.hellothomas.jedi.consumer.domain.User;
import xyz.hellothomas.jedi.consumer.domain.UserExample;
import xyz.hellothomas.jedi.consumer.infrastructure.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 80234613 唐圆
 * @date 2021/7/7 9:13
 * @descripton
 * @version 1.0
 */
@Slf4j
@Service
public class LocalMessageSender implements MessageSender {
    private final ConcurrentHashMap<String, String> emailMap = new ConcurrentHashMap<>();
    private final UserMapper userMapper;

    public LocalMessageSender(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public void notify(List<String> users, String msg) {
        cacheEmailsFromDB(users);

        users.stream().forEach(i -> {
            if (emailMap.containsKey(i)) {
                log.warn("notify {} : {}", emailMap.get(i), msg);
            } else {
                log.warn("{} 用户不存在", i);
            }
        });
    }

    /**
     * 从数据库缓存邮箱
     *
     * @param yishitongIds
     */
    private void cacheEmailsFromDB(List<String> yishitongIds) {
        List<String> newEmails = new ArrayList<>();
        yishitongIds.stream().forEach(i -> {
            if (!emailMap.containsKey(i)) {
                newEmails.add(i);
            }
        });

        if (newEmails.isEmpty()) {
            return;
        }

        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserNameIn(newEmails)
                .andEnabledEqualTo(true);

        List<User> users = userMapper.selectByExample(userExample);

        users.forEach(i -> emailMap.put(i.getUserName(), i.getEmail()));
    }
}
