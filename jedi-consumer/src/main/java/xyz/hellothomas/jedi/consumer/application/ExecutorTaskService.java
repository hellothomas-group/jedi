package xyz.hellothomas.jedi.consumer.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTask;
import xyz.hellothomas.jedi.consumer.domain.ExecutorTaskExample;
import xyz.hellothomas.jedi.consumer.infrastructure.mapper.ExecutorTaskMapper;

import java.time.LocalDateTime;
import java.util.List;

import static xyz.hellothomas.jedi.consumer.common.constants.Constants.CAFFEINE_CACHE_NAME_EXECUTOR_TASK;

/**
 * @author Thomas
 * @date 2021/3/7 23:18
 * @description
 * @version 1.0
 */
@Slf4j
@Service
public class ExecutorTaskService {
    private final ExecutorTaskMapper executorTaskMapper;

    public ExecutorTaskService(ExecutorTaskMapper executorTaskMapper) {
        this.executorTaskMapper = executorTaskMapper;
    }

    public List<ExecutorTask> findTaskList(String namespaceName, String appId) {
        ExecutorTaskExample executorTaskExample = new ExecutorTaskExample();
        executorTaskExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdEqualTo(appId)
                .andUpdateTimeGreaterThan(LocalDateTime.now().minusDays(1));
        return executorTaskMapper.selectByExample(executorTaskExample);
    }

    @Cacheable(cacheNames = CAFFEINE_CACHE_NAME_EXECUTOR_TASK, key = "#namespaceName + '+' + #appId + '+' + " +
            "#executorName + '+' + #taskName", cacheManager = "caffeineCacheManager", unless = "#result == null")
    public ExecutorTask findTask(String namespaceName, String appId, String executorName, String taskName) {
        ExecutorTaskExample executorTaskExample = new ExecutorTaskExample();
        executorTaskExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdEqualTo(appId)
                .andExecutorNameEqualTo(executorName)
                .andTaskNameEqualTo(taskName)
                // todo
                .andUpdateTimeGreaterThan(LocalDateTime.now().minusHours(1));
        List<ExecutorTask> executorTaskList = executorTaskMapper.selectByExample(executorTaskExample);

        return executorTaskList.isEmpty() ? null : executorTaskList.get(0);
    }

    @CachePut(cacheNames = CAFFEINE_CACHE_NAME_EXECUTOR_TASK, key = "#namespaceName + '+' + #appId + '+' + " +
            "#executorName + '+' + #taskName", cacheManager = "caffeineCacheManager", unless = "#result == null")
    public ExecutorTask saveTask(String namespaceName, String appId, String executorName, String taskName) {
        ExecutorTask executorTask = new ExecutorTask();
        executorTask.setNamespaceName(namespaceName);
        executorTask.setAppId(appId);
        executorTask.setExecutorName(executorName);
        executorTask.setTaskName(taskName);
        executorTask.setIsDeleted(false);
        executorTask.setCreateUser("admin");
        executorTask.setUpdateUser("admin");
        LocalDateTime currentDateTime = LocalDateTime.now();
        executorTask.setCreateTime(currentDateTime);
        executorTask.setUpdateTime(currentDateTime);

        executorTaskMapper.insertOrUpdate(executorTask);
        return executorTask;
    }
}
