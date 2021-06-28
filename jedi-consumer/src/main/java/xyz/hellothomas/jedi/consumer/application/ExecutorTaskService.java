package xyz.hellothomas.jedi.consumer.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskMessage;
import xyz.hellothomas.jedi.consumer.infrastructure.mapper.ExecutorTaskMessageMapper;
import xyz.hellothomas.jedi.core.dto.consumer.ExecutorTaskNotification;
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
public class ExecutorTaskService implements NotificationService<ExecutorTaskNotification> {
    private final ExecutorTaskMessageMapper executorTaskMessageMapper;

    public ExecutorTaskService(ExecutorTaskMessageMapper executorTaskMessageMapper) {
        this.executorTaskMessageMapper = executorTaskMessageMapper;
    }

    @Override
    public void process(ExecutorTaskNotification executorTaskNotification) {
        ExecutorTaskMessage executorTaskMessage = new ExecutorTaskMessage();
        BeanUtils.copyProperties(executorTaskNotification, executorTaskMessage);
        executorTaskMessage.setCreateTime(LocalDateTime.now());
        executorTaskMessage.setUpdateTime(LocalDateTime.now());
        log.info("executorTaskMessage:{}", executorTaskMessage);
        executorTaskMessageMapper.insert(executorTaskMessage);
    }

    @Override
    public boolean match(ExecutorTaskNotification notification) {
        return MessageType.EXECUTOR_TASK.getTypeValue().equals(notification.getMessageType());
    }
}
