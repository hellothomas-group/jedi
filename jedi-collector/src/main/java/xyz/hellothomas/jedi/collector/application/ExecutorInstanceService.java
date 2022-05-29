package xyz.hellothomas.jedi.collector.application;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import xyz.hellothomas.jedi.collector.api.dto.PageHelperRequest;
import xyz.hellothomas.jedi.collector.api.dto.PageResult;
import xyz.hellothomas.jedi.collector.domain.ExecutorInstance;
import xyz.hellothomas.jedi.collector.domain.ExecutorInstanceExample;
import xyz.hellothomas.jedi.collector.infrastructure.mapper.ExecutorInstanceMapper;

import java.time.LocalDateTime;
import java.util.List;

import static xyz.hellothomas.jedi.biz.common.constants.Constants.DEFAULT_PAGE_SIZE;
import static xyz.hellothomas.jedi.collector.common.constants.Constants.CAFFEINE_CACHE_NAME_EXECUTOR_INSTANCE;

/**
 * @author Thomas
 * @date 2021/11/7 18:37
 * @description
 * @version 1.0
 */
@Slf4j
@Service
public class ExecutorInstanceService {
    private static final int INSTANCE_EXPIRE_HOURS = 6;
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
        if (executorInstance.getUpdateTime().isBefore(LocalDateTime.now().minusHours(INSTANCE_EXPIRE_HOURS))) {
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
        executorInstance.setCreateTime(currentDateTime);
        executorInstance.setUpdateTime(currentDateTime);

        executorInstanceMapper.insertOrUpdate(executorInstance);
        return executorInstance;
    }

    public PageResult<ExecutorInstance> findInstances(String namespaceName, String appId, String executorName,
                                                      PageHelperRequest pageHelperRequest) {
        ExecutorInstanceExample executorInstanceExample = new ExecutorInstanceExample();
        executorInstanceExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdEqualTo(appId)
                .andExecutorNameEqualTo(executorName)
                .andUpdateTimeGreaterThan(LocalDateTime.now().minusHours(INSTANCE_EXPIRE_HOURS + 1));

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
