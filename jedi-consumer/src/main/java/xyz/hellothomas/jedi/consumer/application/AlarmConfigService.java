package xyz.hellothomas.jedi.consumer.application;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.hellothomas.jedi.consumer.domain.AlarmConfig;
import xyz.hellothomas.jedi.consumer.domain.AlarmConfigExample;
import xyz.hellothomas.jedi.consumer.infrastructure.mapper.AlarmConfigMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlarmConfigService {

    private final AlarmConfigMapper alarmConfigMapper;

    public AlarmConfigService(AlarmConfigMapper alarmConfigMapper) {
        this.alarmConfigMapper = alarmConfigMapper;
    }

    @Transactional
    public AlarmConfig delete(long id, String operator) {
        AlarmConfig alarmConfig = alarmConfigMapper.selectByPrimaryKey(id);
        if (alarmConfig == null) {
            throw new IllegalArgumentException("alarmConfig not exist. ID:" + id);
        }

        alarmConfig.setIsDeleted(true);
        alarmConfig.setDataChangeLastModifiedBy(operator);
        alarmConfig.setDataChangeLastModifiedTime(LocalDateTime.now());
        alarmConfigMapper.updateByPrimaryKey(alarmConfig);

        return alarmConfig;
    }

    @Transactional
    public int deleteByExecutor(String namespaceName, String appId, String executorName, String operator) {
        AlarmConfigExample alarmConfigExample = new AlarmConfigExample();
        alarmConfigExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdEqualTo(appId)
                .andExecutorNameEqualTo(executorName);

        AlarmConfig alarmConfig = new AlarmConfig();
        alarmConfig.setIsDeleted(true);
        alarmConfig.setDataChangeLastModifiedBy(operator);
        alarmConfig.setDataChangeLastModifiedTime(LocalDateTime.now());
        return alarmConfigMapper.updateByExampleSelective(alarmConfig, alarmConfigExample);
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


    @Transactional
    public AlarmConfig save(String namespaceName, String appId, String executorName, String configuration,
                            String operator) {
        AlarmConfig alarmConfig = new AlarmConfig();
        alarmConfig.setNamespaceName(namespaceName);
        alarmConfig.setAppId(appId);
        alarmConfig.setExecutorName(executorName);
        alarmConfig.setConfiguration(configuration);

        LocalDateTime currentDateTime = LocalDateTime.now();
        alarmConfig.setDataChangeCreatedBy(operator);
        alarmConfig.setDataChangeCreatedTime(currentDateTime);
        alarmConfig.setDataChangeLastModifiedBy(operator);
        alarmConfig.setDataChangeLastModifiedTime(currentDateTime);

        alarmConfigMapper.insertSelective(alarmConfig);

        return alarmConfig;
    }

    @Transactional
    public int update(AlarmConfig alarmConfig) {
        return alarmConfigMapper.updateByPrimaryKey(alarmConfig);
    }
}
