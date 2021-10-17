package xyz.hellothomas.jedi.admin.api;

import com.google.common.base.Splitter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.hellothomas.jedi.admin.api.dto.*;
import xyz.hellothomas.jedi.admin.application.InstanceService;
import xyz.hellothomas.jedi.admin.application.ReleaseService;
import xyz.hellothomas.jedi.biz.common.utils.LocalBeanUtils;
import xyz.hellothomas.jedi.biz.domain.config.Instance;
import xyz.hellothomas.jedi.biz.domain.config.InstanceConfig;
import xyz.hellothomas.jedi.biz.domain.config.Release;
import xyz.hellothomas.jedi.biz.infrastructure.exception.NotFoundException;
import xyz.hellothomas.jedi.core.dto.ApiResponse;

import java.util.*;
import java.util.stream.Collectors;

@Api(value = "instance", tags = "instance")
@RestController
@RequestMapping("/instances")
public class InstanceConfigController {
    private static final Splitter RELEASES_SPLITTER = Splitter.on(",").omitEmptyStrings()
            .trimResults();
    private final ReleaseService releaseService;
    private final InstanceService instanceService;

    public InstanceConfigController(final ReleaseService releaseService, final InstanceService instanceService) {
        this.releaseService = releaseService;
        this.instanceService = instanceService;
    }

    @GetMapping("/by-release")
    public ApiResponse<PageResult<InstanceResponse>> getByRelease(@RequestParam("releaseId") long releaseId,
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
        return ApiResponse.success(PageResult.<InstanceResponse>builder()
                .content(instanceResponses)
                .total(instanceConfigsPage.getTotal())
                .pageNum(instanceConfigsPage.getPageNum())
                .pageSize(instanceConfigsPage.getPageSize())
                .build());
    }

    @GetMapping("/by-executor-and-releases-not-in")
    public ApiResponse<List<InstanceResponse>> getByReleasesNotIn(@RequestParam("namespaceName") String namespaceName,
                                                                  @RequestParam("appId") String appId,
                                                                  @RequestParam("executorName") String executorName,
                                                                  @RequestParam("releaseIds") String releaseIds) {
        Set<Long> releaseIdSet = RELEASES_SPLITTER.splitToList(releaseIds).stream().map(Long::parseLong)
                .collect(Collectors.toSet());

        List<Release> releases = releaseService.findByReleaseIds(releaseIdSet);

        if (CollectionUtils.isEmpty(releases)) {
            throw new NotFoundException(String.format("releases not found for %s", releaseIds));
        }

        Set<String> releaseKeys = releases.stream().map(Release::getReleaseKey).collect(Collectors
                .toSet());

        List<InstanceConfig> instanceConfigs = instanceService
                .findInstanceConfigsByExecutorWithReleaseKeysNotIn(namespaceName, appId, executorName,
                        releaseKeys);

        if (CollectionUtils.isEmpty(instanceConfigs)) {
            return ApiResponse.success(Collections.emptyList());
        }

        Multimap<Long, InstanceConfig> instanceConfigMap = HashMultimap.create();
        Set<String> otherReleaseKeys = Sets.newHashSet();

        for (InstanceConfig instanceConfig : instanceConfigs) {
            instanceConfigMap.put(instanceConfig.getInstanceId(), instanceConfig);
            otherReleaseKeys.add(instanceConfig.getReleaseKey());
        }

        List<Instance> instances = instanceService.findInstancesByIds(instanceConfigMap.keySet());

        List<InstanceResponse> instanceResponses = LocalBeanUtils.batchTransform(InstanceResponse.class, instances);

        List<Release> otherReleases = releaseService.findByReleaseKeys(otherReleaseKeys);
        Map<String, ReleaseResponse> releaseMap = Maps.newHashMap();

        for (Release release : otherReleases) {
            //unset configurations to save space
            release.setConfigurations(null);
            ReleaseResponse releaseResponse = LocalBeanUtils.transform(ReleaseResponse.class, release);
            releaseMap.put(release.getReleaseKey(), releaseResponse);
        }

        for (InstanceResponse instanceResponse : instanceResponses) {
            Collection<InstanceConfig> configs = instanceConfigMap.get(instanceResponse.getId());
            List<InstanceConfigResponse> configResponses = configs.stream().map(instanceConfig -> {
                InstanceConfigResponse instanceConfigResponse = new InstanceConfigResponse();
                instanceConfigResponse.setRelease(releaseMap.get(instanceConfig.getReleaseKey()));
                instanceConfigResponse.setReleaseDeliveryTime(instanceConfig.getReleaseDeliveryTime());
                instanceConfigResponse.setDataChangeLastModifiedTime(instanceConfig
                        .getDataChangeLastModifiedTime());
                return instanceConfigResponse;
            }).collect(Collectors.toList());
            instanceResponse.setConfigs(configResponses);
        }

        return ApiResponse.success(instanceResponses);
    }

    @GetMapping("/by-namespace")
    public ApiResponse<PageResult<InstanceResponse>> getInstancesByExecutor(
            @RequestParam("namespaceName") String namespaceName, @RequestParam("appId") String appId,
            @RequestParam("executorName") String executorName,
            @RequestParam(value = "instanceId", required = false) String instanceId,
            PageHelperRequest pageHelperRequest) {
        PageResult<Instance> instancesPage = instanceService.findInstancesByExecutor(namespaceName, appId, executorName,
                pageHelperRequest);

        return ApiResponse.success(transform2PageResult(instancesPage));
    }

    @GetMapping("/by-namespace/count")
    public ApiResponse<Long> getInstancesCountByNamespace(@RequestParam("namespaceName") String namespaceName,
                                                          @RequestParam("appId") String appId,
                                                          @RequestParam("executorName") String executorName,
                                                          PageHelperRequest pageHelperRequest) {
        PageResult<Instance> instances = instanceService.findInstancesByExecutor(namespaceName, appId,
                executorName, pageHelperRequest);
        return ApiResponse.success(instances.getTotal());
    }

    private PageResult<InstanceResponse> transform2PageResult(PageResult<Instance> instancePageResult) {
        List<Instance> instances = instancePageResult.getContent();
        List<InstanceResponse> instanceResponses = new ArrayList<>(instances.size());
        for (Instance instance : instances) {
            instanceResponses.add(LocalBeanUtils.transform(InstanceResponse.class, instance));
        }

        return new PageResult<>(instanceResponses, instancePageResult.getTotal(), instancePageResult.getPageNum(),
                instancePageResult.getPageSize());
    }
}
