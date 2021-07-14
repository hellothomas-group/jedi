package xyz.hellothomas.jedi.consumer.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import xyz.hellothomas.jedi.consumer.domain.ExecutorShutdownMessage;
import xyz.hellothomas.jedi.consumer.infrastructure.mapper.ExecutorShutdownMessageMapper;
import xyz.hellothomas.jedi.core.dto.consumer.ExecutorShutdownNotification;
import xyz.hellothomas.jedi.core.enums.MessageType;

import java.time.LocalDateTime;

/**
 * @author Thomas
 * @date 2021/3/7 23:18
 * @description
 * @version 1.0
 */
@Slf4j
@Service
public class ExecutorShutdownService implements NotificationService<ExecutorShutdownNotification> {
    private final ExecutorShutdownMessageMapper executorShutdownMessageMapper;

    public ExecutorShutdownService(ExecutorShutdownMessageMapper executorShutdownMessageMapper) {
        this.executorShutdownMessageMapper = executorShutdownMessageMapper;
    }

    @Override
    public void process(ExecutorShutdownNotification executorShutdownNotification) {
        ExecutorShutdownMessage executorShutdownMessage = new ExecutorShutdownMessage();
        BeanUtils.copyProperties(executorShutdownNotification, executorShutdownMessage);
        executorShutdownMessage.setCreateTime(LocalDateTime.now());
        executorShutdownMessage.setUpdateTime(LocalDateTime.now());
        log.info("executorShutdownMessage:{}", executorShutdownMessage);
        executorShutdownMessageMapper.insertSelective(executorShutdownMessage);
    }

    @Override
    public boolean match(ExecutorShutdownNotification notification) {
        return MessageType.EXECUTOR_SHUTDOWN.getTypeValue().equals(notification.getMessageType());
    }
}
