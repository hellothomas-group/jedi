package xyz.hellothomas.jedi.consumer.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.hellothomas.jedi.consumer.domain.ExecutorShutdownMessageExample;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskMessageExample;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTickerMessageExample;
import xyz.hellothomas.jedi.consumer.domain.MonitorMessageExample;
import xyz.hellothomas.jedi.consumer.infrastructure.mapper.ExecutorShutdownMessageMapper;
import xyz.hellothomas.jedi.consumer.infrastructure.mapper.ExecutorTaskMessageMapper;
import xyz.hellothomas.jedi.consumer.infrastructure.mapper.ExecutorTickerMessageMapper;
import xyz.hellothomas.jedi.consumer.infrastructure.mapper.MonitorMessageMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author 80234613 唐圆
 * @date 2021/8/6 12:50
 * @descripton
 * @version 1.0
 */
@Slf4j
@Service
public class ClearDataService {
    private final TaskLockService taskLockService;
    private final ExecutorTaskMessageMapper executorTaskMessageMapper;
    private final ExecutorTickerMessageMapper executorTickerMessageMapper;
    private final ExecutorShutdownMessageMapper executorShutdownMessageMapper;
    private final MonitorMessageMapper monitorMessageMapper;

    public ClearDataService(TaskLockService taskLockService, ExecutorTaskMessageMapper executorTaskMessageMapper,
                            ExecutorTickerMessageMapper executorTickerMessageMapper,
                            ExecutorShutdownMessageMapper executorShutdownMessageMapper,
                            MonitorMessageMapper monitorMessageMapper) {
        this.taskLockService = taskLockService;
        this.executorTaskMessageMapper = executorTaskMessageMapper;
        this.executorTickerMessageMapper = executorTickerMessageMapper;
        this.executorShutdownMessageMapper = executorShutdownMessageMapper;
        this.monitorMessageMapper = monitorMessageMapper;
    }

    public void clearMonitorMessage(LocalDate currentDate) {
        LocalDateTime clearDateTime = currentDate.minusDays(10).atStartOfDay();
        try {
            MonitorMessageExample monitorMessageExample = new MonitorMessageExample();
            monitorMessageExample.createCriteria().andUpdateTimeLessThan(clearDateTime);
            monitorMessageMapper.deleteByExample(monitorMessageExample);
            log.info("clearMonitorMessage before {} success", clearDateTime);
        } catch (Exception e) {
            log.error(String.format("clearMonitorMessage before %s error", clearDateTime.toString()), e);
        }
    }

    public void clearExecutorShutdownMessage(LocalDate currentDate) {
        LocalDateTime clearDateTime = currentDate.minusDays(10).atStartOfDay();
        try {
            ExecutorShutdownMessageExample executorShutdownMessageExample = new ExecutorShutdownMessageExample();
            executorShutdownMessageExample.createCriteria().andUpdateTimeLessThan(clearDateTime);
            executorShutdownMessageMapper.deleteByExample(executorShutdownMessageExample);
            log.info("clearExecutorShutdownMessage before {} success", clearDateTime);
        } catch (Exception e) {
            log.error(String.format("clearExecutorShutdownMessage before %s error", clearDateTime.toString()), e);
        }
    }

    public void clearExecutorTaskMessage(LocalDate currentDate) {
        LocalDateTime clearDateTime = currentDate.minusDays(10).atStartOfDay();
        try {
            ExecutorTaskMessageExample executorTaskMessageExample = new ExecutorTaskMessageExample();
            executorTaskMessageExample.createCriteria().andUpdateTimeLessThan(clearDateTime);
            executorTaskMessageMapper.deleteByExample(executorTaskMessageExample);
            log.info("clearExecutorTaskMessage before {} success", clearDateTime);
        } catch (Exception e) {
            log.error(String.format("clearExecutorTaskMessage before %s error", clearDateTime.toString()), e);
        }
    }

    public void clearExecutorTickMessage(LocalDate currentDate) {
        LocalDateTime clearDateTime = currentDate.minusDays(10).atStartOfDay();
        try {
            ExecutorTickerMessageExample executorTickerMessageExample = new ExecutorTickerMessageExample();
            executorTickerMessageExample.createCriteria().andUpdateTimeLessThan(clearDateTime);
            executorTickerMessageMapper.deleteByExample(executorTickerMessageExample);
            log.info("clearExecutorTickMessage before {} success", clearDateTime);
        } catch (Exception e) {
            log.error(String.format("clearExecutorTickMessage before %s error", clearDateTime.toString()), e);
        }
    }
}
