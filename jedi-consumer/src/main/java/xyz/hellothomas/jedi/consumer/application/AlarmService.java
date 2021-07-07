package xyz.hellothomas.jedi.consumer.application;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 80234613 唐圆
 * @date 2021/6/28 14:30
 * @descripton
 * @version 1.0
 */
@Service
@Slf4j
public class AlarmService {
    @Value("${spring.profiles.active}")
    private String env;
    private final MessageSender messageSender;

    public AlarmService(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public List<String> getUser(String namespaceName, String appId, String executorName) {
        return Lists.newArrayList("234613");
    }

    public void notify(String namespaceName, String appId, String executorName, String msg) {
        List<String> notifyUsers = getUser(namespaceName, appId, executorName);
        messageSender.notify(notifyUsers, String.format("[%s环境] %s", env, msg));
    }
}
