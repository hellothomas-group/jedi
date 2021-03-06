package xyz.hellothomas.jedi.config.application;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.hellothomas.jedi.biz.domain.config.Instance;
import xyz.hellothomas.jedi.biz.domain.config.InstanceConfig;
import xyz.hellothomas.jedi.biz.domain.config.InstanceConfigExample;
import xyz.hellothomas.jedi.biz.domain.config.InstanceExample;
import xyz.hellothomas.jedi.biz.infrastructure.mapper.config.InstanceConfigMapper;
import xyz.hellothomas.jedi.biz.infrastructure.mapper.config.InstanceMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InstanceService {
    private final InstanceMapper instanceMapper;
    private final InstanceConfigMapper instanceConfigMapper;

    public InstanceService(
            final InstanceMapper instanceMapper,
            final InstanceConfigMapper instanceConfigMapper) {
        this.instanceMapper = instanceMapper;
        this.instanceConfigMapper = instanceConfigMapper;
    }

    public Instance findInstance(String namespaceName, String appId, String ip) {
        InstanceExample instanceExample = new InstanceExample();
        instanceExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdEqualTo(appId)
                .andIpEqualTo(ip);

        List<Instance> instances = instanceMapper.selectByExample(instanceExample);

        return instances.isEmpty() ? null : instances.get(0);
    }

    @Transactional
    public Instance createInstance(String namespaceName, String appId, String ip) {
        Instance instance = new Instance();
        instance.setNamespaceName(namespaceName);
        instance.setAppId(appId);
        instance.setIp(ip);
        LocalDateTime currentDateTime = LocalDateTime.now();
        instance.setCreateTime(currentDateTime);
        instance.setUpdateTime(currentDateTime);

        instanceMapper.insert(instance);

        return instance;
    }

    @Transactional
    public Instance createInstance(Instance instance) {
        instanceMapper.insert(instance);

        return instance;
    }

    public InstanceConfig findInstanceConfig(long instanceId, String configAppId, String
            configExecutorName) {
        InstanceConfigExample instanceConfigExample = new InstanceConfigExample();
        instanceConfigExample.createCriteria().andInstanceIdEqualTo(instanceId)
                .andConfigAppIdEqualTo(configAppId)
                .andConfigExecutorNameEqualTo(configExecutorName);
        List<InstanceConfig> instanceConfigs = instanceConfigMapper.selectByExample(instanceConfigExample);

        return instanceConfigs.isEmpty() ? null : instanceConfigs.get(0);
    }

    /**
     * Currently the instance config is expired by 1 day, add one more hour to avoid possible time
     * difference
     */
    private LocalDateTime getValidInstanceConfigDate() {
        return LocalDateTime.now().minusDays(1).minusHours(1);
    }

    @Transactional
    public InstanceConfig createInstanceConfig(InstanceConfig instanceConfig) {

        instanceConfigMapper.insertSelective(instanceConfig);

        return instanceConfig;
    }

    @Transactional
    public InstanceConfig updateInstanceConfig(InstanceConfig instanceConfig) {
        InstanceConfig existedInstanceConfig = instanceConfigMapper.selectByPrimaryKey(instanceConfig.getId());
        Preconditions.checkArgument(existedInstanceConfig != null, String.format(
                "Instance config %d doesn't exist", instanceConfig.getId()));

        existedInstanceConfig.setReleaseKey(instanceConfig.getReleaseKey());
        existedInstanceConfig.setReleaseDeliveryTime(instanceConfig.getReleaseDeliveryTime());
        existedInstanceConfig.setUpdateTime(instanceConfig
                .getUpdateTime());

        instanceConfigMapper.updateByPrimaryKeySelective(existedInstanceConfig);

        return existedInstanceConfig;
    }
}
