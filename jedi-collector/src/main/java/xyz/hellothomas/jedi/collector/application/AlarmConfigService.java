package xyz.hellothomas.jedi.collector.application;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import xyz.hellothomas.jedi.biz.domain.monitor.AlarmConfig;
import xyz.hellothomas.jedi.biz.domain.monitor.AlarmConfigExample;
import xyz.hellothomas.jedi.collector.infrastructure.mapper.AlarmConfigMapper;

import java.time.LocalDateTime;
import java.util.List;

import static xyz.hellothomas.jedi.collector.common.constants.Constants.CAFFEINE_CACHE_NAME_EXECUTOR_ALARM;

@Slf4j
@Service
public class AlarmConfigService {

    private final AlarmConfigMapper alarmConfigMapper;

    public AlarmConfigService(AlarmConfigMapper alarmConfigMapper) {
        this.alarmConfigMapper = alarmConfigMapper;
    }

    public AlarmConfig delete(long id, String operator) {
        AlarmConfig alarmConfig = alarmConfigMapper.selectByPrimaryKey(id);
        if (alarmConfig == null) {
            throw new IllegalArgumentException("alarmConfig not exist. ID:" + id);
        }

        alarmConfig.setIsDeleted(true);
        alarmConfig.setUpdateUser(operator);
        alarmConfig.setUpdateTime(LocalDateTime.now());
        alarmConfigMapper.updateByPrimaryKey(alarmConfig);

        return alarmConfig;
    }

    public int deleteByExecutor(String namespaceName, String appId, String executorName, String operator) {
        AlarmConfigExample alarmConfigExample = new AlarmConfigExample();
        alarmConfigExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdEqualTo(appId)
                .andExecutorNameEqualTo(executorName);

        AlarmConfig alarmConfig = new AlarmConfig();
        alarmConfig.setIsDeleted(true);
        alarmConfig.setUpdateUser(operator);
        alarmConfig.setUpdateTime(LocalDateTime.now());
        return alarmConfigMapper.updateByExampleSelective(alarmConfig, alarmConfigExample);
    }

    @Cacheable(cacheNames = CAFFEINE_CACHE_NAME_EXECUTOR_ALARM, key = "#namespaceName + '+' + #appId + '+' + #executorName",
            cacheManager = "caffeineCacheManager", unless = "#result == null")
    public AlarmConfig findOneCache(String namespaceName, String appId, String executorName) {
        return findOne(namespaceName, appId, executorName);
    }

    public AlarmConfig findOne(String namespaceName, String appId, String executorName) {
        AlarmConfigExample alarmConfigExample = new AlarmConfigExample();
        alarmConfigExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdEqualTo(appId)
                .andExecutorNameEqualTo(executorName);

        List<AlarmConfig> alarmConfigs = alarmConfigMapper.selectByExample(alarmConfigExample);
        return alarmConfigs.size() == 0 ? null : alarmConfigs.get(0);
    }

    public AlarmConfig findOne(Long alarmConfigId) {
        AlarmConfig alarmConfig = alarmConfigMapper.selectByPrimaryKey(alarmConfigId);

        if (alarmConfig != null && alarmConfig.getIsDeleted()) {
            return null;
        }

        return alarmConfig;
    }

    @CachePut(cacheNames = CAFFEINE_CACHE_NAME_EXECUTOR_ALARM, key = "#namespaceName + '+' + #appId + '+' + " +
            "#executorName", cacheManager = "caffeineCacheManager", unless = "#result == null")
    public AlarmConfig save(String namespaceName, String appId, String executorName, String configuration,
                            String operator) {
        AlarmConfig alarmConfig = new AlarmConfig();
        alarmConfig.setNamespaceName(namespaceName);
        alarmConfig.setAppId(appId);
        alarmConfig.setExecutorName(executorName);
        alarmConfig.setConfiguration(configuration);

        LocalDateTime currentDateTime = LocalDateTime.now();
        alarmConfig.setCreateUser(operator);
        alarmConfig.setCreateTime(currentDateTime);
        alarmConfig.setUpdateUser(operator);
        alarmConfig.setUpdateTime(currentDateTime);

        alarmConfigMapper.insertSelective(alarmConfig);

        return alarmConfig;
    }

    @CacheEvict(cacheNames = CAFFEINE_CACHE_NAME_EXECUTOR_ALARM, key = "#alarmConfig.namespaceName + '+' + #alarmConfig.appId " +
            "+" + "'+' + #alarmConfig.executorName", cacheManager = "caffeineCacheManager")
    public int update(AlarmConfig alarmConfig) {
        return alarmConfigMapper.updateByPrimaryKey(alarmConfig);
    }
}
