package xyz.hellothomas.jedi.admin.api;

import io.swagger.annotations.Api;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;
import xyz.hellothomas.jedi.admin.api.dto.AppRequest;
import xyz.hellothomas.jedi.admin.api.dto.AppResponse;
import xyz.hellothomas.jedi.admin.api.dto.PageHelperRequest;
import xyz.hellothomas.jedi.admin.api.dto.PageResult;
import xyz.hellothomas.jedi.admin.application.AppService;
import xyz.hellothomas.jedi.admin.common.enums.AdminErrorCodeEnum;
import xyz.hellothomas.jedi.admin.common.enums.RoleTypeEnum;
import xyz.hellothomas.jedi.admin.infrastructure.annotation.PreAuthorize;
import xyz.hellothomas.jedi.admin.infrastructure.annotation.UserLoginToken;
import xyz.hellothomas.jedi.admin.infrastructure.listener.PermissionInitEvent;
import xyz.hellothomas.jedi.admin.infrastructure.listener.SyncEvent;
import xyz.hellothomas.jedi.biz.common.enums.SyncOperationEnum;
import xyz.hellothomas.jedi.biz.common.enums.SyncTypeEnum;
import xyz.hellothomas.jedi.biz.common.utils.LocalBeanUtils;
import xyz.hellothomas.jedi.biz.domain.monitor.App;
import xyz.hellothomas.jedi.core.dto.ApiResponse;
import xyz.hellothomas.jedi.core.exception.BusinessException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static xyz.hellothomas.jedi.admin.common.utils.JwtUtil.CLAIM_USER_NAME;

/**
 * @author Thomas
 */
@UserLoginToken
@Api(value = "app", tags = "app")
@RestController
public class AppController {
    private static final Pattern APP_PATTERN = Pattern.compile("^[A-Za-z][A-Za-z0-9-_.]{0,31}$");
    private final AppService appService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public AppController(AppService appService, ApplicationEventPublisher applicationEventPublisher) {
        this.appService = appService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PreAuthorize(value = "@permissionValidator.hasCreateOrDeleteAppPermission(#operator)")
    @PostMapping("/namespaces/{namespaceName}/apps/{appId}")
    public ApiResponse<AppResponse> create(@PathVariable("namespaceName") String namespaceName,
                                           @Valid @RequestBody AppRequest appRequest,
                                           @RequestAttribute(CLAIM_USER_NAME) String operator) {
        // 命名检查
        Matcher matcher = APP_PATTERN.matcher(appRequest.getAppId());
        if (!matcher.matches()) {
            throw new BusinessException(AdminErrorCodeEnum.APP_INVALID);
        }

        App entity = LocalBeanUtils.transform(App.class, appRequest);
        App managedEntity = appService.findOne(entity.getNamespaceName(), entity.getAppId());

        if (managedEntity != null) {
            throw new BusinessException(AdminErrorCodeEnum.APP_EXIST);
        }

        entity = appService.save(entity, operator);

        applicationEventPublisher.publishEvent(new SyncEvent(entity, SyncTypeEnum.APP, SyncOperationEnum.CREATION));
        applicationEventPublisher.publishEvent(new PermissionInitEvent(entity, RoleTypeEnum.APP_MANAGER));

        return ApiResponse.success(LocalBeanUtils.transform(AppResponse.class, entity));
    }

    @PreAuthorize(value = "@permissionValidator.hasModifyAppPermission(#namespaceName, #appId, #operator)")
    @PutMapping("/namespaces/{namespaceName}/apps/{appId}")
    public ApiResponse<String> update(@PathVariable String namespaceName, @PathVariable String appId,
                                      @RequestBody AppRequest request,
                                      @RequestAttribute(CLAIM_USER_NAME) String operator) {
        if (!Objects.equals(namespaceName, request.getNamespaceName())) {
            throw new BusinessException(AdminErrorCodeEnum.APP_REQUEST_ERROR);
        }

        App entity = LocalBeanUtils.transform(App.class, request);

        entity = appService.update(entity, operator);

        applicationEventPublisher.publishEvent(new SyncEvent(entity, SyncTypeEnum.APP, SyncOperationEnum.CHANGE));

        return ApiResponse.success("更新成功");
    }

    @DeleteMapping("/namespaces/{namespaceName}/apps/{appId}")
    public ApiResponse<String> delete(@PathVariable("namespaceName") String namespaceName,
                                      @PathVariable("appId") String appId,
                                      @RequestAttribute(CLAIM_USER_NAME) String operator) {
        App entity = appService.findOne(namespaceName, appId);
        if (entity == null) {
            throw new BusinessException(AdminErrorCodeEnum.APP_NOT_EXIST);
        }
        appService.deleteApp(entity, operator);

        applicationEventPublisher.publishEvent(new SyncEvent(entity, SyncTypeEnum.APP, SyncOperationEnum.DELETION));

        return ApiResponse.success("删除成功");
    }

    @GetMapping("/namespaces/{namespaceName}/apps")
    public ApiResponse<PageResult<AppResponse>> getAppNamespaces(@PathVariable("namespaceName") String namespaceName,
                                                                 PageHelperRequest pageHelperRequest) {
        PageResult<App> appsPage = appService.findByNamespaceName(namespaceName, pageHelperRequest);

        return ApiResponse.success(transform2PageResult(appsPage));
    }

    @GetMapping("/namespaces/{namespaceName}/apps/{appId}")
    public ApiResponse<AppResponse> getAppNamespaces(@PathVariable("namespaceName") String namespaceName,
                                                     @PathVariable("appId") String appId) {
        App app = appService.findOne(namespaceName, appId);

        return ApiResponse.success(LocalBeanUtils.transform(AppResponse.class, app));
    }

    private PageResult<AppResponse> transform2PageResult(PageResult<App> appPageResult) {
        List<App> apps = appPageResult.getContent();
        List<AppResponse> appResponses = new ArrayList<>(apps.size());
        for (App app : apps) {
            appResponses.add(LocalBeanUtils.transform(AppResponse.class, app));
        }

        return new PageResult<>(appResponses, appPageResult.getTotal(), appPageResult.getPageNum(),
                appPageResult.getPageSize());
    }
}
