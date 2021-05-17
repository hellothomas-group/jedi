package xyz.hellothomas.jedi.consumer.application;

import xyz.hellothomas.jedi.consumer.domain.MonitorMessage;
import xyz.hellothomas.jedi.consumer.infrastructure.mapper.MonitorMessageMapper;
import xyz.hellothomas.jedi.core.dto.consumer.CustomNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author Thomas
 * @date 2021/3/7 23:18
 * @description
 * @version 1.0
 */
@Slf4j
@Service
public class CustomMessageService {
    private final MonitorMessageMapper monitorMessageMapper;

    public CustomMessageService(MonitorMessageMapper monitorMessageMapper) {
        this.monitorMessageMapper = monitorMessageMapper;
    }

    public void save(CustomNotification customNotification) {
        MonitorMessage monitorMessage = new MonitorMessage();
        BeanUtils.copyProperties(customNotification, monitorMessage);
        monitorMessage.setCreateTime(LocalDateTime.now());
        monitorMessage.setUpdateTime(LocalDateTime.now());
        log.info("monitorMessage:{}", monitorMessage);
        monitorMessageMapper.insert(monitorMessage);
    }
}
