package com.hellothomas.jedi.admin.api;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.hellothomas.jedi.admin.api.dto.InstanceConfigResponse;
import com.hellothomas.jedi.admin.api.dto.InstanceResponse;
import com.hellothomas.jedi.admin.api.dto.PageHelperRequest;
import com.hellothomas.jedi.admin.api.dto.PageResult;
import com.hellothomas.jedi.admin.application.InstanceService;
import com.hellothomas.jedi.admin.application.ReleaseService;
import com.hellothomas.jedi.biz.common.utils.LocalBeanUtils;
import com.hellothomas.jedi.biz.domain.Instance;
import com.hellothomas.jedi.biz.domain.InstanceConfig;
import com.hellothomas.jedi.biz.domain.Release;
import com.hellothomas.jedi.biz.infrastructure.exception.NotFoundException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/instances")
public class InstanceConfigController {
    private final ReleaseService releaseService;
    private final InstanceService instanceService;

    public InstanceConfigController(final ReleaseService releaseService, final InstanceService instanceService) {
        this.releaseService = releaseService;
        this.instanceService = instanceService;
    }

    @GetMapping("/by-release")
    public PageResult<InstanceResponse> getByRelease(@RequestParam("releaseId") long releaseId,
                                                     PageHelperRequest pageHelperRequest) {
        Release release = releaseService.findOne(releaseId);
        if (release == null) {
            throw new NotFoundException(String.format("release not found for %s", releaseId));
        }
        PageResult<InstanceConfig> instanceConfigsPage = instanceService.findActiveInstanceConfigsByReleaseKey
                (release.getReleaseKey(), pageHelperRequest);

        List<InstanceResponse> instanceResponses = Collections.emptyList();

        if (!instanceConfigsPage.getContent().isEmpty()) {
            Multimap<Long, InstanceConfig> instanceConfigMap = HashMultimap.create();
            Set<String> otherReleaseKeys = Sets.newHashSet();

            for (InstanceConfig instanceConfig : instanceConfigsPage.getContent()) {
                instanceConfigMap.put(instanceConfig.getInstanceId(), instanceConfig);
                otherReleaseKeys.add(instanceConfig.getReleaseKey());
            }

            Set<Long> instanceIds = instanceConfigMap.keySet();

            List<Instance> instances = instanceService.findInstancesByIds(instanceIds);

            if (!CollectionUtils.isEmpty(instances)) {
                instanceResponses = LocalBeanUtils.batchTransform(InstanceResponse.class, instances);
            }

            for (InstanceResponse instanceResponse : instanceResponses) {
                Collection<InstanceConfig> configs = instanceConfigMap.get(instanceResponse.getId());
                List<InstanceConfigResponse> configDTOs = configs.stream().map(instanceConfig -> {
                    InstanceConfigResponse instanceConfigResponse = new InstanceConfigResponse();
                    //to save some space
                    instanceConfigResponse.setRelease(null);
                    instanceConfigResponse.setReleaseDeliveryTime(instanceConfig.getReleaseDeliveryTime());
                    instanceConfigResponse.setDataChangeLastModifiedTime(instanceConfig
                            .getDataChangeLastModifiedTime());
                    return instanceConfigResponse;
                }).collect(Collectors.toList());
                instanceResponse.setConfigs(configDTOs);
            }
        }
        return PageResult.<InstanceResponse>builder()
                .content(instanceResponses)
                .total(instanceConfigsPage.getTotal())
                .pageNum(instanceConfigsPage.getPageNum())
                .pageSize(instanceConfigsPage.getPageSize())
                .build();
    }

    @GetMapping("/by-namespace/count")
    public long getInstancesCountByNamespace(@RequestParam("namespaceName") String namespaceName,
                                             @RequestParam("appId") String appId,
                                             @RequestParam("executorName") String executorName,
                                             PageHelperRequest pageHelperRequest) {
        PageResult<Instance> instances = instanceService.findInstancesByExecutor(namespaceName, appId,
                executorName, pageHelperRequest);
        return instances.getTotal();
    }
}
