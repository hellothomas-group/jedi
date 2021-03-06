package xyz.hellothomas.jedi.admin.application;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.hellothomas.jedi.admin.api.dto.PageHelperRequest;
import xyz.hellothomas.jedi.admin.api.dto.PageResult;
import xyz.hellothomas.jedi.admin.common.enums.ReleaseOperationEnum;
import xyz.hellothomas.jedi.admin.common.utils.ReleaseKeyGenerator;
import xyz.hellothomas.jedi.admin.domain.Audit;
import xyz.hellothomas.jedi.admin.domain.Executor;
import xyz.hellothomas.jedi.admin.domain.Item;
import xyz.hellothomas.jedi.biz.domain.config.Release;
import xyz.hellothomas.jedi.biz.domain.config.ReleaseExample;
import xyz.hellothomas.jedi.biz.infrastructure.exception.BadRequestException;
import xyz.hellothomas.jedi.biz.infrastructure.exception.NotFoundException;
import xyz.hellothomas.jedi.biz.infrastructure.mapper.config.ReleaseMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static xyz.hellothomas.jedi.biz.common.constants.Constants.DEFAULT_PAGE_SIZE;

@Service
public class ReleaseService {

    private static final String IS_EMERGENCY_PUBLISH = "isEmergencyPublish";

    private final ReleaseMapper releaseMapper;
    private final ItemService itemService;
    private final AuditService auditService;
    private final ExecutorLockService executorLockService;
    private final ReleaseHistoryService releaseHistoryService;

    public ReleaseService(
            final ReleaseMapper releaseMapper,
            final ItemService itemService,
            final AuditService auditService,
            final ExecutorLockService executorLockService,
            final ReleaseHistoryService releaseHistoryService) {
        this.releaseMapper = releaseMapper;
        this.itemService = itemService;
        this.auditService = auditService;
        this.executorLockService = executorLockService;
        this.releaseHistoryService = releaseHistoryService;
    }

    public Release findOne(long releaseId) {
        return releaseMapper.selectByPrimaryKey(releaseId);
    }

    public List<Release> findByReleaseIds(Set<Long> releaseIds) {
        ReleaseExample releaseExample = new ReleaseExample();
        releaseExample.createCriteria().andIdIn(Lists.newArrayList(releaseIds))
                .andIsAbandonedEqualTo(false);
        return releaseMapper.selectByExample(releaseExample);
    }

    public List<Release> findByReleaseKeys(Set<String> releaseKeys) {
        ReleaseExample releaseExample = new ReleaseExample();
        releaseExample.createCriteria().andReleaseKeyIn(Lists.newArrayList(releaseKeys))
                .andIsAbandonedEqualTo(false);
        return releaseMapper.selectByExample(releaseExample);
    }

    public Release findLatestActiveRelease(Executor executor) {
        return findLatestActiveRelease(executor.getNamespaceName(),
                executor.getAppId(), executor.getExecutorName());

    }

    public Release findLatestActiveRelease(String namespaceName, String appId, String executorName) {
        ReleaseExample releaseExample = new ReleaseExample();
        releaseExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdEqualTo(appId)
                .andExecutorNameEqualTo(executorName)
                .andIsAbandonedEqualTo(false);
        releaseExample.setOrderByClause("id desc limit 1");
        List<Release> result = releaseMapper.selectByExample(releaseExample);
        return result.isEmpty() ? null : result.get(0);
    }

    public PageResult<Release> findAllReleases(String namespaceName, String appId, String executorName,
                                               PageHelperRequest pageHelperRequest) {
        ReleaseExample releaseExample = new ReleaseExample();
        releaseExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdEqualTo(appId)
                .andExecutorNameEqualTo(executorName);
        releaseExample.setOrderByClause("id desc");

        int pageSize = pageHelperRequest.getPageSize();
        int pageNum = pageHelperRequest.getPageNum();
        pageSize = (pageSize <= 0) ? DEFAULT_PAGE_SIZE : pageSize;
        PageHelper.startPage(pageNum, pageSize);

        List<Release> releases = releaseMapper.selectByExample(releaseExample);
        PageInfo<Release> pageInfo = new PageInfo<>(releases);

        return PageResult.<Release>builder()
                .content(pageInfo.getList())
                .total(pageInfo.getTotal())
                .pageNum(pageInfo.getPageNum())
                .pageSize(pageInfo.getPageSize())
                .build();
    }

    public PageResult<Release> findActiveReleases(String namespaceName, String appId, String executorName,
                                                  PageHelperRequest pageHelperRequest) {
        ReleaseExample releaseExample = new ReleaseExample();
        releaseExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdEqualTo(appId)
                .andExecutorNameEqualTo(executorName)
                .andIsAbandonedEqualTo(false);
        releaseExample.setOrderByClause("id desc");

        int pageSize = pageHelperRequest.getPageSize();
        int pageNum = pageHelperRequest.getPageNum();
        pageSize = (pageSize <= 0) ? DEFAULT_PAGE_SIZE : pageSize;
        PageHelper.startPage(pageNum, pageSize);

        List<Release> releases = releaseMapper.selectByExample(releaseExample);
        PageInfo<Release> pageInfo = new PageInfo<>(releases);

        return PageResult.<Release>builder()
                .content(pageInfo.getList())
                .total(pageInfo.getTotal())
                .pageNum(pageInfo.getPageNum())
                .pageSize(pageInfo.getPageSize())
                .build();
    }

    private List<Release> findActiveReleasesBetween(String namespaceName, String appId, String executorName,
                                                    long fromReleaseId, long toReleaseId) {
        ReleaseExample releaseExample = new ReleaseExample();
        releaseExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdEqualTo(appId)
                .andExecutorNameEqualTo(executorName)
                .andIsAbandonedEqualTo(false)
                .andIdBetween(fromReleaseId, toReleaseId);
        releaseExample.setOrderByClause("id desc");

        return releaseMapper.selectByExample(releaseExample);
    }

    @Transactional
    public Release publish(Executor executor, String releaseName, String releaseComment,
                           String operator, boolean isEmergencyPublish) {
        Item item = itemService.findOneByExecutorId(executor.getId());

        if (item == null || item.getIsDeleted()) {
            throw new BadRequestException("item not exist");
        }

        //master release
        Map<String, Object> operationContext = Maps.newLinkedHashMap();
        operationContext.put(IS_EMERGENCY_PUBLISH, isEmergencyPublish);

        Release release = masterRelease(executor, releaseName, releaseComment, item.getConfiguration(),
                operator, ReleaseOperationEnum.NORMAL_RELEASE.getValue(), operationContext);

        return release;
    }

    private Release masterRelease(Executor executor, String releaseName, String releaseComment,
                                  String configuration, String operator,
                                  int releaseOperation, Map<String, Object> operationContext) {
        Release lastActiveRelease = findLatestActiveRelease(executor);
        long previousReleaseId = lastActiveRelease == null ? 0 : lastActiveRelease.getId();
        Release release = createRelease(executor, releaseName, releaseComment,
                configuration, operator);

        releaseHistoryService.createReleaseHistory(executor.getNamespaceName(), executor.getAppId(),
                executor.getExecutorName(), release.getId(), previousReleaseId, releaseOperation,
                operationContext, operator);

        return release;
    }

    private Release createRelease(Executor executor, String name, String comment,
                                  String configuration, String operator) {
        Release release = new Release();
        release.setReleaseKey(ReleaseKeyGenerator.generateReleaseKey(executor));
        release.setName(name);
        release.setNamespaceName(executor.getNamespaceName());
        release.setAppId(executor.getAppId());
        release.setExecutorName(executor.getExecutorName());
        release.setConfigurations(configuration);
        release.setComment(comment);
        release.setIsAbandoned(false);

        LocalDateTime currentDateTime = LocalDateTime.now();
        release.setCreateTime(currentDateTime);
        release.setCreateUser(operator);
        release.setUpdateTime(currentDateTime);
        release.setUpdateUser(operator);
        releaseMapper.insertSelective(release);

        executorLockService.unlock(executor.getId());
        auditService.audit(Release.class.getSimpleName(), release.getId(), Audit.OP.INSERT,
                release.getCreateUser());

        return release;
    }

    @Transactional
    public Release rollback(long releaseId, String operator) {
        Release release = findOne(releaseId);
        if (release == null) {
            throw new NotFoundException("release not found");
        }
        if (release.getIsAbandoned()) {
            throw new BadRequestException("release is not active");
        }

        String namespaceName = release.getNamespaceName();
        String appId = release.getAppId();
        String executorName = release.getExecutorName();

        PageHelperRequest pageHelperRequest = new PageHelperRequest();
        pageHelperRequest.setPageNum(1);
        pageHelperRequest.setPageSize(2);
        PageResult<Release> twoLatestActiveReleases = findActiveReleases(namespaceName, appId, executorName,
                pageHelperRequest);
        if (twoLatestActiveReleases == null || twoLatestActiveReleases.getContent() == null || twoLatestActiveReleases.getContent().size() < 2) {
            throw new BadRequestException(String.format(
                    "Can't rollback executor(namespaceName=%s, appId=%s, executorName=%s) because there is only one " +
                            "active release",
                    namespaceName,
                    appId,
                    executorName));
        }

        release.setIsAbandoned(true);
        release.setUpdateUser(operator);

        releaseMapper.insert(release);

        releaseHistoryService.createReleaseHistory(namespaceName, appId, executorName,
                twoLatestActiveReleases.getContent().get(1).getId(), release.getId(),
                ReleaseOperationEnum.ROLLBACK.getValue(),
                null, operator);

        return release;
    }

    @Transactional
    public Release rollbackTo(long releaseId, long toReleaseId, String operator) {
        if (releaseId == toReleaseId) {
            throw new BadRequestException("current release equal to target release");
        }

        Release release = findOne(releaseId);
        Release toRelease = findOne(toReleaseId);
        if (release == null || toRelease == null) {
            throw new NotFoundException("release not found");
        }
        if (release.getIsAbandoned() || toRelease.getIsAbandoned()) {
            throw new BadRequestException("release is not active");
        }

        String namespaceName = release.getNamespaceName();
        String appId = release.getAppId();
        String executorName = release.getExecutorName();

        List<Release> releases = findActiveReleasesBetween(namespaceName, appId, executorName,
                toReleaseId, releaseId);

        for (int i = 0; i < releases.size() - 1; i++) {
            releases.get(i).setIsAbandoned(true);
            releases.get(i).setUpdateUser(operator);

            ReleaseExample releaseExample = new ReleaseExample();
            releaseExample.createCriteria().andIdEqualTo(releases.get(i).getId());

            releaseMapper.updateByExampleSelective(releases.get(i), releaseExample);
        }

        releaseHistoryService.createReleaseHistory(namespaceName, appId,
                executorName, toReleaseId, release.getId(), ReleaseOperationEnum.ROLLBACK.getValue(), null, operator);

        return release;
    }

    @Transactional
    public int batchDelete(String namespaceName, String appId, String executorName, String operator) {
        ReleaseExample releaseExample = new ReleaseExample();
        releaseExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdEqualTo(appId)
                .andExecutorNameEqualTo(executorName)
                .andIsDeletedEqualTo(false);
        Release release = new Release();
        release.setIsDeleted(true);
        release.setUpdateTime(LocalDateTime.now());
        release.setUpdateUser(operator);
        return releaseMapper.updateByExampleSelective(release, releaseExample);
    }

}
