package xyz.hellothomas.jedi.admin.application;


import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.hellothomas.jedi.admin.domain.AlarmConfig;
import xyz.hellothomas.jedi.admin.domain.AlarmConfigExample;
import xyz.hellothomas.jedi.admin.domain.Audit;
import xyz.hellothomas.jedi.admin.domain.Executor;
import xyz.hellothomas.jedi.admin.infrastructure.mapper.AlarmConfigMapper;
import xyz.hellothomas.jedi.biz.infrastructure.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlarmConfigService {

    private final AlarmConfigMapper alarmConfigMapper;
    private final ExecutorService executorService;
    private final AuditService auditService;

    public AlarmConfigService(
            final AlarmConfigMapper alarmConfigMapper,
            final @Lazy ExecutorService executorService,
            final AuditService auditService) {
        this.alarmConfigMapper = alarmConfigMapper;
        this.executorService = executorService;
        this.auditService = auditService;
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

        auditService.audit(AlarmConfig.class.getSimpleName(), id, Audit.OP.DELETE, operator);
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
        Executor executor = executorService.findOne(namespaceName, appId, executorName);
        if (executor == null) {
            throw new NotFoundException(
                    String.format("executor not found for %s %s %s", namespaceName, appId, executorName));
        }

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

        auditService.audit(AlarmConfig.class.getSimpleName(), alarmConfig.getId(), Audit.OP.INSERT,
                alarmConfig.getDataChangeCreatedBy());



        return alarmConfig;
    }

    @Transactional
    public int update(AlarmConfig alarmConfig) {
        int updatedRows = alarmConfigMapper.updateByPrimaryKey(alarmConfig);

        auditService.audit(AlarmConfig.class.getSimpleName(), alarmConfig.getId(), Audit.OP.UPDATE,
                alarmConfig.getDataChangeLastModifiedBy());

        return updatedRows;
    }
}
