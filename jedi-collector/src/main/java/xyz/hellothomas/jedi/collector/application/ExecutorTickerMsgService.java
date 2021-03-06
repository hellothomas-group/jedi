package xyz.hellothomas.jedi.collector.application;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import xyz.hellothomas.jedi.biz.domain.monitor.AlarmConfig;
import xyz.hellothomas.jedi.collector.api.dto.PageHelperRequest;
import xyz.hellothomas.jedi.collector.api.dto.PageResult;
import xyz.hellothomas.jedi.collector.domain.ExecutorTickerMessage;
import xyz.hellothomas.jedi.collector.domain.ExecutorTickerMessageExample;
import xyz.hellothomas.jedi.collector.infrastructure.mapper.ExecutorTickerMessageMapper;
import xyz.hellothomas.jedi.core.dto.collector.ExecutorTickerNotification;
import xyz.hellothomas.jedi.core.enums.MessageType;
import xyz.hellothomas.jedi.core.utils.JsonUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

/**
 * @author Thomas
 * @date 2021/3/7 23:18
 * @description
 * @version 1.0
 */
@Slf4j
@Service
public class ExecutorTickerMsgService implements NotificationService<ExecutorTickerNotification> {
    private final ExecutorTickerMessageMapper executorTickerMessageMapper;
    private final AlarmConfigService alarmConfigService;
    private final AlarmService alarmService;
    private final ExecutorInstanceService executorInstanceService;

    public ExecutorTickerMsgService(ExecutorTickerMessageMapper executorTickerMessageMapper,
                                    AlarmConfigService alarmConfigService, AlarmService alarmService,
                                    ExecutorInstanceService executorInstanceService) {
        this.executorTickerMessageMapper = executorTickerMessageMapper;
        this.alarmConfigService = alarmConfigService;
        this.alarmService = alarmService;
        this.executorInstanceService = executorInstanceService;
    }

    @Override
    public void process(ExecutorTickerNotification executorTickerNotification) {
        alarm(executorTickerNotification);

        ExecutorTickerMessage executorTickerMessage = new ExecutorTickerMessage();
        BeanUtils.copyProperties(executorTickerNotification, executorTickerMessage);
        executorTickerMessage.setCreateTime(LocalDateTime.now());
        executorTickerMessage.setUpdateTime(LocalDateTime.now());
        log.debug("executorTickerMessage:{}", executorTickerMessage);
        executorTickerMessageMapper.insertSelective(executorTickerMessage);
    }

    @Override
    public void process(List<ExecutorTickerNotification> notifications) {
        List<ExecutorTickerMessage> executorTickerMessages = new ArrayList<>(notifications.size());
        notifications.stream().forEach(i -> {
            alarm(i);

            ExecutorTickerMessage executorTickerMessage = new ExecutorTickerMessage();
            BeanUtils.copyProperties(i, executorTickerMessage);
            executorTickerMessage.setCreateTime(LocalDateTime.now());
            executorTickerMessage.setUpdateTime(LocalDateTime.now());
            executorTickerMessages.add(executorTickerMessage);

            recordInstance(executorTickerMessage);
        });
        try {
            executorTickerMessageMapper.insertBatch(executorTickerMessages);
        } catch (Exception e) {
            executorTickerMessageMapper.insertOrUpdateBatch(executorTickerMessages);
        }
    }

    @Override
    public boolean match(ExecutorTickerNotification notification) {
        return MessageType.EXECUTOR_TICKER.getTypeValue().equals(notification.getMessageType());
    }

    public PageResult<ExecutorTickerMessage> findByExecutorHostAndRecordTime(String namespaceName, String appId,
                                                                             String executorName,
                                                                             String instanceIp, LocalDateTime startTime,
                                                                             LocalDateTime endTime,
                                                                             PageHelperRequest pageHelperRequest) {
        ExecutorTickerMessageExample executorTickerMessageExample = new ExecutorTickerMessageExample();
        executorTickerMessageExample.createCriteria().andNamespaceEqualTo(namespaceName)
                .andAppIdEqualTo(appId)
                .andPoolNameEqualTo(executorName)
                .andHostEqualTo(instanceIp)
                .andRecordTimeBetween(startTime, endTime);
        executorTickerMessageExample.setOrderByClause("record_time");

        int pageSize = pageHelperRequest.getPageSize();
        int pageNum = pageHelperRequest.getPageNum();
        pageSize = (pageSize <= 0) ? DEFAULT_PAGE_SIZE : pageSize;
        PageHelper.startPage(pageNum, pageSize);

        List<ExecutorTickerMessage> executorTickerMessages =
                executorTickerMessageMapper.selectByExample(executorTickerMessageExample);
        PageInfo<ExecutorTickerMessage> pageInfo = new PageInfo<>(executorTickerMessages);

        return PageResult.<ExecutorTickerMessage>builder()
                .content(pageInfo.getList())
                .total(pageInfo.getTotal())
                .pageNum(pageInfo.getPageNum())
                .pageSize(pageInfo.getPageSize())
                .build();
    }

    private void alarm(ExecutorTickerNotification notification) {
        AlarmConfig alarmConfig = alarmConfigService.findOneCache(notification.getNamespace(), notification.getAppId(),
                notification.getPoolName());
        // ???????????????
        if (alarmConfig == null) {
            return;
        }

        AlarmConfig.ConfigurationProperty configurationProperty = JsonUtil.deserialize(alarmConfig.getConfiguration()
                , AlarmConfig.ConfigurationProperty.class);

        // ?????????????????????
        if (!configurationProperty.isAlarmEnabled()) {
            return;
        }

        String msg = EMPTY;
        if (configurationProperty.getQueueThreshold() > 0 && notification.getQueueSize() > configurationProperty.getQueueThreshold()) {
            msg += String.format("?????????????????????:%d;", notification.getQueueSize());
        }
        BigDecimal poolActivation =
                new BigDecimal(notification.getActiveCount()).divide(new BigDecimal(notification.getMaximumPoolSize()), 2, BigDecimal.ROUND_HALF_UP);
        if (configurationProperty.getPoolActivationThreshold() > 0
                && poolActivation.compareTo(BigDecimal.valueOf(configurationProperty.getPoolActivationThreshold()).divide(new BigDecimal(100))) > 0) {
            msg += String.format(" ???????????????:%s;", poolActivation);
        }

        if (configurationProperty.getRejectCountThreshold() >= 0 && notification.getRejectCount() > configurationProperty.getRejectCountThreshold() && notification.getRejectCount() != notification.getLastRejectCount()) {
            msg += String.format(" ???????????????:%d;", notification.getRejectCount());
        }

        if (StringUtils.isNotBlank(msg)) {
            alarmService.notify(notification.getNamespace(), notification.getAppId(), notification.getPoolName(),
                    msg);
        }
    }

    private void recordInstance(ExecutorTickerMessage executorTickerMessage) {
        if (executorInstanceService.findInstance(executorTickerMessage.getNamespace(),
                executorTickerMessage.getAppId(), executorTickerMessage.getPoolName(),
                executorTickerMessage.getHost()) == null) {
            executorInstanceService.saveInstance(executorTickerMessage.getNamespace(),
                    executorTickerMessage.getAppId(), executorTickerMessage.getPoolName(),
                    executorTickerMessage.getHost());
        }
    }
}
