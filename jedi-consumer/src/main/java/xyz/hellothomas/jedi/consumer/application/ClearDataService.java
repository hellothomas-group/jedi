package xyz.hellothomas.jedi.consumer.application;

import org.springframework.scheduling.annotation.Scheduled;
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
@Service
public class ClearDataService {
    private static final String CLEAR_CLIENT_MESSAGE_NAME = "CLEAR_CLIENT_MESSAGE";
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

    /**
     * D-30日数据清理
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void clear() {
        LocalDate currentDate = LocalDate.now();
        // 创建D日数据清理任务
        if (taskLockService.insertTaskLock(currentDate, CLEAR_CLIENT_MESSAGE_NAME) == 0) {
            return;
        }

        // 清理D-30日客户端消息
        LocalDateTime clearDateTime = currentDate.minusDays(30).atStartOfDay();
        ExecutorTickerMessageExample executorTickerMessageExample = new ExecutorTickerMessageExample();
        executorTickerMessageExample.createCriteria().andUpdateTimeLessThan(clearDateTime);
        executorTickerMessageMapper.deleteByExample(executorTickerMessageExample);

        ExecutorTaskMessageExample executorTaskMessageExample = new ExecutorTaskMessageExample();
        executorTaskMessageExample.createCriteria().andUpdateTimeLessThan(clearDateTime);
        executorTaskMessageMapper.deleteByExample(executorTaskMessageExample);

        ExecutorShutdownMessageExample executorShutdownMessageExample = new ExecutorShutdownMessageExample();
        executorShutdownMessageExample.createCriteria().andUpdateTimeLessThan(clearDateTime);
        executorShutdownMessageMapper.deleteByExample(executorShutdownMessageExample);

        MonitorMessageExample monitorMessageExample = new MonitorMessageExample();
        monitorMessageExample.createCriteria().andUpdateTimeLessThan(clearDateTime);
        monitorMessageMapper.deleteByExample(monitorMessageExample);
    }
}
