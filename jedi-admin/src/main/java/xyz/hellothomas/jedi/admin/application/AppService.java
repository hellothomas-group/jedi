package xyz.hellothomas.jedi.admin.application;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.hellothomas.jedi.admin.domain.*;
import xyz.hellothomas.jedi.admin.infrastructure.mapper.AppMapper;
import xyz.hellothomas.jedi.biz.infrastructure.exception.BadRequestException;
import xyz.hellothomas.jedi.biz.infrastructure.exception.ServiceException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
public class AppService {


    private final AppMapper appMapper;
    private final AuditService auditService;
    private final ExecutorService executorService;

    public AppService(AppMapper appMapper, AuditService auditService, ExecutorService executorService) {
        this.appMapper = appMapper;
        this.auditService = auditService;
        this.executorService = executorService;
    }

    public boolean isNamespaceNameAppUnique(String namespaceName, String appId) {
        Objects.requireNonNull(namespaceName, "Namespace must not be null");
        Objects.requireNonNull(appId, "AppId must not be null");
        AppExample appExample = new AppExample();
        appExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdEqualTo(appId);
        return appMapper.countByExample(appExample) == 0;
    }

    public List<App> findByNamespaceName(String namespaceName) {
        AppExample appExample = new AppExample();
        appExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andIsDeletedEqualTo(false);

        return appMapper.selectByExample(appExample);
    }

    public App findOne(String namespaceName, String appId) {
        Preconditions
                .checkArgument(!StringUtils.isAnyBlank(namespaceName, appId), "Namespace or appId must not" +
                        " be null");
        AppExample appExample = new AppExample();
        appExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdEqualTo(appId)
                .andIsDeletedEqualTo(false);

        List<App> apps = appMapper.selectByExample(appExample);

        return apps.isEmpty() ? null : apps.get(0);
    }

    public List<App> findByNamespaceAndAppIds(String namespaceName, Set<String> AppIds) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(namespaceName), "namespaceName must not be null");
        if (AppIds == null || AppIds.isEmpty()) {
            return Collections.emptyList();
        }

        AppExample appExample = new AppExample();
        appExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdIn(Lists.newArrayList(AppIds))
                .andIsDeletedEqualTo(false);

        return appMapper.selectByExample(appExample);
    }

    @Transactional
    public App save(App app, String operator) {
        if (!isNamespaceNameAppUnique(app.getNamespaceName(), app.getAppId())) {
            throw new ServiceException("namespace app not unique");
        }

        LocalDateTime currentDateTime = LocalDateTime.now();
        app.setDataChangeCreatedTime(currentDateTime);
        app.setDataChangeCreatedBy(operator);
        app.setDataChangeLastModifiedTime(currentDateTime);
        app.setDataChangeLastModifiedBy(operator);

        appMapper.insertSelective(app);

        auditService.audit(App.class.getSimpleName(), app.getId(), Audit.OP.INSERT, operator);
        return app;
    }

    public App update(App app, String operator) {
        String namespaceName = app.getNamespaceName();
        String appId = app.getAppId();
        App managedApp = findOne(namespaceName, appId);
        if (managedApp == null) {
            throw new BadRequestException(String.format("App not exists. namespaceName= %s appId= %s", namespaceName,
                    appId));
        }

        managedApp.setAppDescription(app.getAppDescription());
        managedApp.setDataChangeLastModifiedBy(operator);
        managedApp.setDataChangeCreatedTime(LocalDateTime.now());

        appMapper.updateByPrimaryKey(managedApp);

        auditService.audit(App.class.getSimpleName(), managedApp.getId(), Audit.OP.UPDATE,
                managedApp.getDataChangeLastModifiedBy());

        return managedApp;
    }

    @Transactional
    public int batchDelete(String namespaceName, String operator) {
        AppExample AppExample = new AppExample();

        AppExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andIsDeletedEqualTo(false);
        App app = new App();
        app.setIsDeleted(true);
        app.setDataChangeLastModifiedTime(LocalDateTime.now());
        app.setDataChangeLastModifiedBy(operator);
        return appMapper.updateByExampleSelective(app, AppExample);
    }

    @Transactional
    public void deleteApp(App app, String operator) {
        String namespaceName = app.getNamespaceName();
        String appId = app.getAppId();

        log.info("{} is deleting App, namespace: {}, appId: {}", operator, namespaceName, appId);

        // 1. delete executors
        List<Executor> executors = executorService.findExecutors(namespaceName, appId);

        for (Executor executor : executors) {
            executorService.deleteExecutor(executor, operator);
        }

        // 2. delete app namespace
        app.setIsDeleted(true);
        app.setDataChangeLastModifiedBy(operator);
        app.setDataChangeLastModifiedTime(LocalDateTime.now());

        auditService.audit(Namespace.class.getSimpleName(), app.getId(), Audit.OP.DELETE, operator);

        appMapper.updateByPrimaryKey(app);
    }
}
