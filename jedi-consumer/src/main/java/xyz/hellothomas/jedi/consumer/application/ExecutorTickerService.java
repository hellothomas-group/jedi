package xyz.hellothomas.jedi.consumer.application;

import xyz.hellothomas.jedi.consumer.domain.ExecutorTickerMessage;
import xyz.hellothomas.jedi.consumer.infrastructure.mapper.ExecutorTickerMessageMapper;
import xyz.hellothomas.jedi.core.dto.consumer.ExecutorTickerNotification;
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
public class ExecutorTickerService {
    private final ExecutorTickerMessageMapper executorTickerMessageMapper;

    public ExecutorTickerService(ExecutorTickerMessageMapper executorTickerMessageMapper) {
        this.executorTickerMessageMapper = executorTickerMessageMapper;
    }

    public void save(ExecutorTickerNotification executorTickerNotification) {
        ExecutorTickerMessage executorTickerMessage = new ExecutorTickerMessage();
        BeanUtils.copyProperties(executorTickerNotification, executorTickerMessage);
        executorTickerMessage.setCreateTime(LocalDateTime.now());
        executorTickerMessage.setUpdateTime(LocalDateTime.now());
        log.info("executorTickerMessage:{}", executorTickerMessage);
        executorTickerMessageMapper.insert(executorTickerMessage);
    }
}
