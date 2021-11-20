package xyz.hellothomas.jedi.consumer.application;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import xyz.hellothomas.jedi.consumer.api.dto.PageHelperRequest;
import xyz.hellothomas.jedi.consumer.api.dto.PageResult;
import xyz.hellothomas.jedi.consumer.domain.ExecutorInstance;
import xyz.hellothomas.jedi.consumer.domain.ExecutorInstanceExample;
import xyz.hellothomas.jedi.consumer.infrastructure.mapper.ExecutorInstanceMapper;

import java.time.LocalDateTime;
import java.util.List;

import static xyz.hellothomas.jedi.biz.common.constants.Constants.DEFAULT_PAGE_SIZE;
import static xyz.hellothomas.jedi.consumer.common.constants.Constants.CAFFEINE_CACHE_NAME_EXECUTOR_INSTANCE;

/**
 * @author Thomas
 * @date 2021/11/7 18:37
 * @description
 * @version 1.0
 */
@Slf4j
@Service
public class ExecutorInstanceService {
    private final ExecutorInstanceMapper executorInstanceMapper;

    public ExecutorInstanceService(ExecutorInstanceMapper executorInstanceMapper) {
        this.executorInstanceMapper = executorInstanceMapper;
    }

    @Cacheable(cacheNames = CAFFEINE_CACHE_NAME_EXECUTOR_INSTANCE, key = "#namespaceName + '+' + #appId + '+' + " +
            "#executorName + '+' + #ip", cacheManager = "caffeineCacheManager", unless = "#result == null")
    public ExecutorInstance findInstance(String namespaceName, String appId, String executorName, String ip) {
        ExecutorInstanceExample executorInstanceExample = new ExecutorInstanceExample();
        executorInstanceExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdEqualTo(appId)
                .andExecutorNameEqualTo(executorName)
                .andIpEqualTo(ip);
        List<ExecutorInstance> executorInstanceList = executorInstanceMapper.selectByExample(executorInstanceExample);
        if (executorInstanceList.isEmpty()) {
            return null;
        }

        ExecutorInstance executorInstance = executorInstanceList.get(0);
        // 6 hour expire
        if (executorInstance.getDataChangeLastModifiedTime().isBefore(LocalDateTime.now().minusHours(6))) {
            return null;
        } else {
            return executorInstance;
        }
    }

    @CachePut(cacheNames = CAFFEINE_CACHE_NAME_EXECUTOR_INSTANCE, key = "#namespaceName + '+' + #appId + '+' + " +
            "#executorName + '+' + #ip", cacheManager = "caffeineCacheManager", unless = "#result == null")
    public ExecutorInstance saveInstance(String namespaceName, String appId, String executorName, String ip) {
        ExecutorInstance executorInstance = new ExecutorInstance();
        executorInstance.setNamespaceName(namespaceName);
        executorInstance.setAppId(appId);
        executorInstance.setExecutorName(executorName);
        executorInstance.setIp(ip);
        LocalDateTime currentDateTime = LocalDateTime.now();
        executorInstance.setDataChangeCreatedTime(currentDateTime);
        executorInstance.setDataChangeLastModifiedTime(currentDateTime);

        executorInstanceMapper.insertOrUpdate(executorInstance);
        return executorInstance;
    }

    public PageResult<ExecutorInstance> findInstances(String namespaceName, String appId, String executorName,
                                                      PageHelperRequest pageHelperRequest) {
        ExecutorInstanceExample executorInstanceExample = new ExecutorInstanceExample();
        executorInstanceExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdEqualTo(appId)
                .andExecutorNameEqualTo(executorName)
                .andDataChangeLastModifiedTimeGreaterThan(LocalDateTime.now().minusHours(6));

        int pageSize = pageHelperRequest.getPageSize();
        int pageNum = pageHelperRequest.getPageNum();
        pageSize = (pageSize <= 0) ? DEFAULT_PAGE_SIZE : pageSize;
        PageHelper.startPage(pageNum, pageSize);

        List<ExecutorInstance> executorInstanceList = executorInstanceMapper.selectByExample(executorInstanceExample);
        PageInfo<ExecutorInstance> pageInfo = new PageInfo<>(executorInstanceList);

        return PageResult.<ExecutorInstance>builder()
                .content(pageInfo.getList())
                .total(pageInfo.getTotal())
                .pageNum(pageInfo.getPageNum())
                .pageSize(pageInfo.getPageSize())
                .build();
    }
}
