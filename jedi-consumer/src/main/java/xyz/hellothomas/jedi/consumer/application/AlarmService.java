package xyz.hellothomas.jedi.consumer.application;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
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
public class AlarmService implements MessageSender {

    public List<String> getUser(String namespaceName, String appId, String executorName) {
        return Lists.newArrayList();
    }

    public void notify(String namespaceName, String appId, String executorName, String msg) {
        List<String> notifyUsers = getUser(namespaceName, appId, executorName);
        notify(notifyUsers, msg);
    }

    @Override
    public void notify(List<String> users, String msg) {
        log.warn("notify {} : {}", String.join(",", users), msg);
    }
}
