package xyz.hellothomas.jedi.collector.application;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import xyz.hellothomas.jedi.collector.api.dto.PageHelperRequest;
import xyz.hellothomas.jedi.collector.api.dto.PageResult;
import xyz.hellothomas.jedi.collector.common.enums.CollectorErrorCodeEnum;
import xyz.hellothomas.jedi.collector.domain.ExecutorTask;
import xyz.hellothomas.jedi.collector.domain.ExecutorTaskMessage;
import xyz.hellothomas.jedi.collector.domain.ExecutorTaskMessageExample;
import xyz.hellothomas.jedi.collector.domain.ExecutorTaskStatistics;
import xyz.hellothomas.jedi.collector.infrastructure.mapper.ExecutorTaskMessageMapper;
import xyz.hellothomas.jedi.core.dto.collector.ExecutorTaskNotification;
import xyz.hellothomas.jedi.core.enums.MessageType;
import xyz.hellothomas.jedi.core.enums.TaskStatusEnum;
import xyz.hellothomas.jedi.core.exception.BusinessException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static xyz.hellothomas.jedi.collector.common.constants.Constants.DEFAULT_PAGE_SIZE;

/**
 * @author Thomas
 * @date 2021/3/7 23:18
 * @description
 * @version 1.0
 */
@Slf4j
@Service
public class ExecutorTaskMsgService implements NotificationService<ExecutorTaskNotification> {
    private final ExecutorTaskMessageMapper executorTaskMessageMapper;
    private final ExecutorTaskService executorTaskService;

    public ExecutorTaskMsgService(ExecutorTaskMessageMapper executorTaskMessageMapper,
                                  ExecutorTaskService executorTaskService) {
        this.executorTaskMessageMapper = executorTaskMessageMapper;
        this.executorTaskService = executorTaskService;
    }

    @Override
    public void process(ExecutorTaskNotification executorTaskNotification) {
        ExecutorTaskMessage executorTaskMessage = new ExecutorTaskMessage();
        BeanUtils.copyProperties(executorTaskNotification, executorTaskMessage);
        // ?????? 0.0.9-version
        if (executorTaskNotification.getStatus() == 0) {
            executorTaskMessage.setStatus(TaskStatusEnum.SUCCESS.getValue());
        }
        if (executorTaskNotification.getEndTime() == null) {
            executorTaskMessage.setEndTime(executorTaskNotification.getRecordTime());
        }
        executorTaskMessage.setCreateTime(LocalDateTime.now());
        executorTaskMessage.setUpdateTime(LocalDateTime.now());
        log.debug("executorTaskMessage:{}", executorTaskMessage);
        executorTaskMessageMapper.insertSelective(executorTaskMessage);
    }

    @Override
    public void process(List<ExecutorTaskNotification> notifications) {
        List<ExecutorTaskMessage> executorTaskMessages = new ArrayList<>(notifications.size());
        notifications.stream().forEach(i -> {
            ExecutorTaskMessage executorTaskMessage = new ExecutorTaskMessage();
            BeanUtils.copyProperties(i, executorTaskMessage);
            // ?????? 0.0.9-version
            if (i.getStatus() == 0) {
                executorTaskMessage.setStatus(TaskStatusEnum.SUCCESS.getValue());
            }
            if (i.getEndTime() == null) {
                executorTaskMessage.setEndTime(i.getRecordTime());
            }
            executorTaskMessage.setCreateTime(LocalDateTime.now());
            executorTaskMessage.setUpdateTime(LocalDateTime.now());
            executorTaskMessages.add(executorTaskMessage);

            // check task is exist
            ExecutorTask executorTask = executorTaskService.findTask(executorTaskMessage.getNamespace(),
                    executorTaskMessage.getAppId(),
                    executorTaskMessage.getPoolName(), executorTaskMessage.getTaskName());
            if (executorTask == null || executorTask.getIsDeleted()) {
                executorTaskService.saveTask(executorTaskMessage.getNamespace(), executorTaskMessage.getAppId(),
                        executorTaskMessage.getPoolName(), executorTaskMessage.getTaskName());
            }
        });

        try {
            executorTaskMessageMapper.insertBatch(executorTaskMessages);
        } catch (Exception e) {
            executorTaskMessageMapper.insertOrUpdateBatch(executorTaskMessages);
        }
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
            if (isSuccess) {
                criteria.andStatusEqualTo(TaskStatusEnum.SUCCESS.getValue());
            } else {
                criteria.andStatusNotEqualTo(TaskStatusEnum.SUCCESS.getValue());
            }
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

    public ExecutorTaskMessage findById(String taskId) {
        ExecutorTaskMessage executorTaskMessage = executorTaskMessageMapper.selectByPrimaryKey(taskId);
        if (executorTaskMessage == null) {
            throw new BusinessException(CollectorErrorCodeEnum.TASK_NOT_EXIST);
        }
        return executorTaskMessage;
    }

    public ExecutorTaskStatistics genTaskStatistics(String namespaceName, String appId, String executorName,
                                                    String taskName, LocalDateTime startTime, LocalDateTime endTime) {
        ExecutorTaskStatistics executorTaskStatistics =
                executorTaskMessageMapper.selectStatisticsByTaskNameAndRecordTime(namespaceName, appId, executorName,
                        taskName, startTime, endTime);
        executorTaskStatistics.setNamespaceName(namespaceName);
        executorTaskStatistics.setAppId(appId);
        executorTaskStatistics.setExecutorName(executorName);
        executorTaskStatistics.setTaskName(taskName);

        return executorTaskStatistics;
    }

    public int updateRetriedTask(String taskId, String newTaskId, String operator) {
        ExecutorTaskMessage executorTaskMessage = new ExecutorTaskMessage();
        executorTaskMessage.setId(taskId);
        executorTaskMessage.setIsRetried(true);
        executorTaskMessage.setRetryId(newTaskId);
        executorTaskMessage.setUpdateTime(LocalDateTime.now());
        executorTaskMessage.setUpdateUser(operator);
        executorTaskMessage.setVersion(2);
        return executorTaskMessageMapper.updateByPrimaryKeySelective(executorTaskMessage);
    }
}
