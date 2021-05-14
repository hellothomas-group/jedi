package com.hellothomas.jedi.consumer.application;

import com.hellothomas.jedi.consumer.domain.ExecutorTaskMessage;
import com.hellothomas.jedi.consumer.infrastructure.mapper.ExecutorTaskMessageMapper;
import com.hellothomas.jedi.core.dto.consumer.ExecutorTaskNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @className ExecutorTaskService
 * @author Thomas
 * @date 2021/3/7 23:18
 * @description
 * @version 1.0
 */
@Slf4j
@Service
public class ExecutorTaskService {
    private final ExecutorTaskMessageMapper executorTaskMessageMapper;

    public ExecutorTaskService(ExecutorTaskMessageMapper executorTaskMessageMapper) {
        this.executorTaskMessageMapper = executorTaskMessageMapper;
    }

    public void save(ExecutorTaskNotification executorTaskNotification) {
        ExecutorTaskMessage executorTaskMessage = new ExecutorTaskMessage();
        BeanUtils.copyProperties(executorTaskNotification, executorTaskMessage);
        executorTaskMessage.setCreateTime(LocalDateTime.now());
        executorTaskMessage.setUpdateTime(LocalDateTime.now());
        log.info("executorTaskMessage:{}", executorTaskMessage);
        executorTaskMessageMapper.insert(executorTaskMessage);
    }
}
