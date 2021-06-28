package xyz.hellothomas.jedi.consumer.application;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import xyz.hellothomas.jedi.consumer.domain.AlarmConfig;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTickerMessage;
import xyz.hellothomas.jedi.consumer.infrastructure.mapper.ExecutorTickerMessageMapper;
import xyz.hellothomas.jedi.core.dto.consumer.ExecutorTickerNotification;
import xyz.hellothomas.jedi.core.enums.MessageType;
import xyz.hellothomas.jedi.core.utils.JsonUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * @author Thomas
 * @date 2021/3/7 23:18
 * @description
 * @version 1.0
 */
@Slf4j
@Service
public class ExecutorTickerService implements NotificationService<ExecutorTickerNotification> {
    private final ExecutorTickerMessageMapper executorTickerMessageMapper;
    private final AlarmConfigService alarmConfigService;
    private final AlarmService alarmService;

    public ExecutorTickerService(ExecutorTickerMessageMapper executorTickerMessageMapper,
                                 AlarmConfigService alarmConfigService, AlarmService alarmService) {
        this.executorTickerMessageMapper = executorTickerMessageMapper;
        this.alarmConfigService = alarmConfigService;
        this.alarmService = alarmService;
    }

    @Override
    public void process(ExecutorTickerNotification executorTickerNotification) {
        alarm(executorTickerNotification);

        ExecutorTickerMessage executorTickerMessage = new ExecutorTickerMessage();
        BeanUtils.copyProperties(executorTickerNotification, executorTickerMessage);
        executorTickerMessage.setCreateTime(LocalDateTime.now());
        executorTickerMessage.setUpdateTime(LocalDateTime.now());
        log.info("executorTickerMessage:{}", executorTickerMessage);
        executorTickerMessageMapper.insert(executorTickerMessage);
    }

    @Override
    public boolean match(ExecutorTickerNotification notification) {
        return MessageType.EXECUTOR_TICKER.getTypeValue().equals(notification.getMessageType());
    }

    private void alarm(ExecutorTickerNotification notification) {
        AlarmConfig alarmConfig = alarmConfigService.findOne(notification.getNamespace(), notification.getAppId(),
                notification.getPoolName());
        // 未配置报警
        if (alarmConfig == null) {
            return;
        }

        AlarmConfig.ConfigurationProperty configurationProperty = JsonUtil.deserialize(alarmConfig.getConfiguration()
                , AlarmConfig.ConfigurationProperty.class);

        // 配置未启用报警
        if (!configurationProperty.isAlarmEnabled()) {
            return;
        }

        String msg = EMPTY;
        if (notification.getQueueSize() > configurationProperty.getQueueThreshold()) {
            msg += String.format("queueSize:%d;", notification.getQueueSize());
        }
        BigDecimal poolActivation =
                new BigDecimal(notification.getActiveCount()).divide(new BigDecimal(notification.getMaximumPoolSize()));
        if (poolActivation.compareTo(configurationProperty.getPoolActivationThreshold()) > 0) {
            msg += String.format("poolActivation:%s;", poolActivation);
        }

        if (notification.getRejectCount() > configurationProperty.getRejectCountThreshold()) {
            msg += String.format("rejectCountThreshold:%d;", notification.getActiveCount());
        }

        if (StringUtils.isNotBlank(msg)) {
            alarmService.notify(notification.getNamespace(), notification.getAppId(), notification.getPoolName(),
                    msg);
        }
    }
}
