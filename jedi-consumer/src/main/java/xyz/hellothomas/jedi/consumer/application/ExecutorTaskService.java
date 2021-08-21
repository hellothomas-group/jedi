package xyz.hellothomas.jedi.consumer.application;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import xyz.hellothomas.jedi.consumer.api.dto.PageHelperRequest;
import xyz.hellothomas.jedi.consumer.api.dto.PageResult;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskMessage;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskMessageExample;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskStatistics;
import xyz.hellothomas.jedi.consumer.domain.pojo.ExecutorTask;
import xyz.hellothomas.jedi.consumer.infrastructure.mapper.ExecutorTaskMessageMapper;
import xyz.hellothomas.jedi.core.dto.consumer.ExecutorTaskNotification;
import xyz.hellothomas.jedi.core.enums.MessageType;

import java.time.LocalDateTime;
import java.util.List;

import static xyz.hellothomas.jedi.consumer.common.constants.Constants.DEFAULT_PAGE_SIZE;

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
        executorTaskMessageMapper.insertSelective(executorTaskMessage);
    }

    @Override
    public boolean match(ExecutorTaskNotification notification) {
        return MessageType.EXECUTOR_TASK.getTypeValue().equals(notification.getMessageType());
    }

    public PageResult<ExecutorTaskMessage> findByTaskNameAndRecordTime(String namespaceName, String appId,
                                                                       String executorName,
                                                                       String taskName, String taskExtraData,
                                                                       Boolean isSuccess, String instanceIp,
                                                                       LocalDateTime startTime, LocalDateTime endTime,
                                                                       PageHelperRequest pageHelperRequest) {
        ExecutorTaskMessageExample executorTaskMessageExample = new ExecutorTaskMessageExample();
        ExecutorTaskMessageExample.Criteria criteria =
                executorTaskMessageExample.createCriteria().andNamespaceEqualTo(namespaceName)
                        .andAppIdEqualTo(appId)
                        .andPoolNameEqualTo(executorName)
                        .andTaskNameEqualTo(taskName)
                        .andRecordTimeBetween(startTime, endTime);
        if (taskExtraData != null) {
            criteria.andTaskExtraDataEqualTo(taskExtraData);
        }
        if (isSuccess != null) {
            criteria.andIsSuccessEqualTo(isSuccess);
        }
        if (instanceIp != null) {
            criteria.andHostEqualTo(instanceIp);
        }

        executorTaskMessageExample.setOrderByClause("record_time");

        int pageSize = pageHelperRequest.getPageSize();
        int pageNum = pageHelperRequest.getPageNum();
        pageSize = (pageSize <= 0) ? DEFAULT_PAGE_SIZE : pageSize;
        PageHelper.startPage(pageNum, pageSize);

        List<ExecutorTaskMessage> executorTaskMessages =
                executorTaskMessageMapper.selectByExample(executorTaskMessageExample);
        PageInfo<ExecutorTaskMessage> pageInfo = new PageInfo<>(executorTaskMessages);

        return PageResult.<ExecutorTaskMessage>builder()
                .content(pageInfo.getList())
                .total(pageInfo.getTotal())
                .pageNum(pageInfo.getPageNum())
                .pageSize(pageInfo.getPageSize())
                .build();
    }

    public List<ExecutorTask> findTasksDistinct(LocalDateTime startTime, LocalDateTime endTime) {
        return executorTaskMessageMapper.selectByRecordTimeAndGroupByTask(startTime, endTime);
    }

    public ExecutorTaskStatistics genTaskStatistics(String namespaceName, String appId, String executorName,
                                                    String taskName, LocalDateTime startTime, LocalDateTime endTime) {
        return executorTaskMessageMapper.selectStatisticsByUniqueKeyAndRecordTime(namespaceName, appId, executorName,
                taskName, startTime, endTime);
    }
}
