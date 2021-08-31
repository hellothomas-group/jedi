package xyz.hellothomas.jedi.consumer.application;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import xyz.hellothomas.jedi.consumer.domain.App;
import xyz.hellothomas.jedi.consumer.domain.AppExample;
import xyz.hellothomas.jedi.consumer.infrastructure.mapper.AppMapper;

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
    private final MessageSender messageSender;
    private final AppMapper appMapper;

    public AlarmService(MessageSender messageSender, AppMapper appMapper) {
        this.messageSender = messageSender;
        this.appMapper = appMapper;
    }

    public List<String> getUser(String namespaceName, String appId, String executorName) {
        AppExample appExample = new AppExample();
        appExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdEqualTo(appId)
                .andIsDeletedEqualTo(false);
        List<App> apps = appMapper.selectByExample(appExample);
        if (apps.isEmpty()) {
            log.warn("环境: {} ,应用: {} 不存在", namespaceName, appId);
            return Lists.newArrayList();
        }
        return Lists.newArrayList(apps.get(0).getDataChangeCreatedBy());
    }

    @Async
    public void notify(String namespaceName, String appId, String executorName, String msg) {
        List<String> notifyUsers = getUser(namespaceName, appId, executorName);
        if (!notifyUsers.isEmpty()) {
            messageSender.notify(notifyUsers, String.format("[%s环境] 应用名:%s 线程池名称:%s, 告警:%s", namespaceName, appId,
                    executorName, msg));
        }
    }
}
