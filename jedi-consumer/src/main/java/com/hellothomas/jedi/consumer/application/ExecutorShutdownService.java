package com.hellothomas.jedi.consumer.application;

import com.hellothomas.jedi.consumer.domain.ExecutorShutdownMessage;
import com.hellothomas.jedi.consumer.infrastructure.mapper.ExecutorShutdownMessageMapper;
import com.hellothomas.jedi.core.dto.consumer.ExecutorShutdownNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @className ExecutorShutdownService
 * @author Thomas
 * @date 2021/3/7 23:18
 * @description
 * @version 1.0
 */
@Slf4j
@Service
public class ExecutorShutdownService {
    private final ExecutorShutdownMessageMapper executorShutdownMessageMapper;

    public ExecutorShutdownService(ExecutorShutdownMessageMapper executorShutdownMessageMapper) {
        this.executorShutdownMessageMapper = executorShutdownMessageMapper;
    }

    public void save(ExecutorShutdownNotification executorShutdownNotification) {
        ExecutorShutdownMessage executorShutdownMessage = new ExecutorShutdownMessage();
        BeanUtils.copyProperties(executorShutdownNotification, executorShutdownMessage);
        executorShutdownMessage.setCreateTime(LocalDateTime.now());
        executorShutdownMessage.setUpdateTime(LocalDateTime.now());
        log.info("executorShutdownMessage:{}", executorShutdownMessage);
        executorShutdownMessageMapper.insert(executorShutdownMessage);
    }
}
