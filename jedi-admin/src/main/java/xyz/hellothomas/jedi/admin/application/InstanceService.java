package xyz.hellothomas.jedi.admin.application;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.hellothomas.jedi.admin.api.dto.PageHelperRequest;
import xyz.hellothomas.jedi.admin.api.dto.PageResult;
import xyz.hellothomas.jedi.biz.domain.config.Instance;
import xyz.hellothomas.jedi.biz.domain.config.InstanceConfig;
import xyz.hellothomas.jedi.biz.domain.config.InstanceConfigExample;
import xyz.hellothomas.jedi.biz.domain.config.InstanceExample;
import xyz.hellothomas.jedi.biz.infrastructure.mapper.config.InstanceConfigMapper;
import xyz.hellothomas.jedi.biz.infrastructure.mapper.config.InstanceMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static xyz.hellothomas.jedi.biz.common.constants.Constants.DEFAULT_PAGE_SIZE;

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

    public List<Instance> findInstancesByIds(Set<Long> instanceIds) {
        InstanceExample instanceExample = new InstanceExample();
        instanceExample.createCriteria().andIdIn(Lists.newArrayList(instanceIds));

        return instanceMapper.selectByExample(instanceExample);
    }

    public PageResult<InstanceConfig> findActiveInstanceConfigsByReleaseKey(String releaseKey,
                                                                            PageHelperRequest pageHelperRequest) {
        InstanceConfigExample instanceConfigExample = new InstanceConfigExample();
        instanceConfigExample.createCriteria().andReleaseKeyEqualTo(releaseKey)
                .andUpdateTimeGreaterThan(getValidInstanceConfigDate());
        instanceConfigExample.setOrderByClause("id");

        int pageSize = pageHelperRequest.getPageSize();
        int pageNum = pageHelperRequest.getPageNum();
        pageSize = (pageSize <= 0) ? DEFAULT_PAGE_SIZE : pageSize;
        PageHelper.startPage(pageNum, pageSize);

        List<InstanceConfig> instanceConfigs = instanceConfigMapper.selectByExample(instanceConfigExample);
        PageInfo<InstanceConfig> pageInfo = new PageInfo<>(instanceConfigs);

        return PageResult.<InstanceConfig>builder()
                .content(pageInfo.getList())
                .total(pageInfo.getTotal())
                .pageNum(pageInfo.getPageNum())
                .pageSize(pageInfo.getPageSize())
                .build();
    }

    public PageResult<Instance> findInstancesByExecutor(String namespaceName, String appId, String
            executorName, PageHelperRequest pageHelperRequest) {
        InstanceConfigExample instanceConfigExample = new InstanceConfigExample();
        instanceConfigExample.createCriteria().andConfigNamespaceNameEqualTo(namespaceName)
                .andConfigAppIdEqualTo(appId)
                .andConfigExecutorNameEqualTo(executorName)
                .andUpdateTimeGreaterThan(getValidInstanceConfigDate());
        instanceConfigExample.setOrderByClause("id");

        int pageSize = pageHelperRequest.getPageSize();
        int pageNum = pageHelperRequest.getPageNum();
        pageSize = (pageSize <= 0) ? DEFAULT_PAGE_SIZE : pageSize;
        PageHelper.startPage(pageNum, pageSize);

        List<InstanceConfig> instanceConfigs = instanceConfigMapper.selectByExample(instanceConfigExample);
        PageInfo<InstanceConfig> pageInfo = new PageInfo<>(instanceConfigs);

        Set<Long> instanceIds = instanceConfigs.stream().map
                (InstanceConfig::getInstanceId).collect(Collectors.toSet());
        List<Instance> instances = findInstancesByIds(instanceIds);

        return PageResult.<Instance>builder()
                .content(instances)
                .total(pageInfo.getTotal())
                .pageNum(pageInfo.getPageNum())
                .pageSize(pageInfo.getPageSize())
                .build();
    }

    public List<InstanceConfig> findInstanceConfigsByExecutorWithReleaseKeysNotIn(String namespaceName,
                                                                                  String appId,
                                                                                  String executorName,
                                                                                  Set<String> releaseKeysNotIn) {
        InstanceConfigExample instanceConfigExample = new InstanceConfigExample();
        instanceConfigExample.createCriteria().andConfigNamespaceNameEqualTo(namespaceName)
                .andConfigAppIdEqualTo(appId)
                .andConfigExecutorNameEqualTo(executorName)
                .andUpdateTimeGreaterThan(getValidInstanceConfigDate())
                .andReleaseKeyNotIn(Lists.newArrayList(releaseKeysNotIn));
        return instanceConfigMapper.selectByExample(instanceConfigExample);
    }

    /**
     * Currently the instance config is expired by 1 day, add one more hour to avoid possible time
     * difference
     */
    private LocalDateTime getValidInstanceConfigDate() {
        return LocalDateTime.now().minusDays(1).minusHours(1);
    }

    @Transactional
    public int batchDeleteInstanceConfig(String configNamespaceName, String configAppId, String configExecutorName) {
        InstanceConfigExample instanceConfigExample = new InstanceConfigExample();
        instanceConfigExample.createCriteria().andConfigNamespaceNameEqualTo(configNamespaceName)
                .andConfigAppIdEqualTo(configAppId)
                .andConfigExecutorNameEqualTo(configExecutorName);
        return instanceConfigMapper.deleteByExample(instanceConfigExample);
    }
}
