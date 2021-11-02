package xyz.hellothomas.jedi.consumer.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import xyz.hellothomas.jedi.consumer.domain.MonitorMessage;
import xyz.hellothomas.jedi.consumer.infrastructure.mapper.MonitorMessageMapper;
import xyz.hellothomas.jedi.core.dto.consumer.CustomNotification;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Thomas
 * @date 2021/3/7 23:18
 * @description
 * @version 1.0
 */
@Slf4j
@Service
public class CustomMsgService implements NotificationService<CustomNotification> {
    private final MonitorMessageMapper monitorMessageMapper;

    public CustomMsgService(MonitorMessageMapper monitorMessageMapper) {
        this.monitorMessageMapper = monitorMessageMapper;
    }

    @Override
    public void process(CustomNotification customNotification) {
        MonitorMessage monitorMessage = new MonitorMessage();
        BeanUtils.copyProperties(customNotification, monitorMessage);
        monitorMessage.setCreateTime(LocalDateTime.now());
        monitorMessage.setUpdateTime(LocalDateTime.now());
        log.debug("monitorMessage:{}", monitorMessage);
        monitorMessageMapper.insert(monitorMessage);
    }

    @Override
    public void process(List<CustomNotification> notifications) {

    }

    @Override
    public boolean match(CustomNotification notification) {
        return null == notification.getMessageType();
    }
}