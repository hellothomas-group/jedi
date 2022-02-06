package xyz.hellothomas.jedi.admin.api;

import io.swagger.annotations.Api;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;
import xyz.hellothomas.jedi.admin.api.dto.ExecutorResponse;
import xyz.hellothomas.jedi.admin.api.dto.PageHelperRequest;
import xyz.hellothomas.jedi.admin.api.dto.PageResult;
import xyz.hellothomas.jedi.admin.application.ExecutorService;
import xyz.hellothomas.jedi.admin.application.ItemService;
import xyz.hellothomas.jedi.admin.application.ReleaseService;
import xyz.hellothomas.jedi.admin.common.enums.AdminErrorCodeEnum;
import xyz.hellothomas.jedi.admin.common.enums.RoleTypeEnum;
import xyz.hellothomas.jedi.admin.domain.Executor;
import xyz.hellothomas.jedi.admin.domain.Item;
import xyz.hellothomas.jedi.admin.infrastructure.annotation.PreAuthorize;
import xyz.hellothomas.jedi.admin.infrastructure.annotation.UserLoginToken;
import xyz.hellothomas.jedi.admin.infrastructure.listener.PermissionInitEvent;
import xyz.hellothomas.jedi.biz.common.utils.LocalBeanUtils;
import xyz.hellothomas.jedi.biz.domain.config.Release;
import xyz.hellothomas.jedi.biz.infrastructure.exception.NotFoundException;
import xyz.hellothomas.jedi.core.dto.ApiResponse;
import xyz.hellothomas.jedi.core.exception.BusinessException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static xyz.hellothomas.jedi.admin.common.utils.JwtUtil.CLAIM_USER_NAME;

@UserLoginToken
@Api(value = "executor", tags = "executor")
@RestController
public class ExecutorController {
    private static final Pattern EXECUTOR_PATTERN = Pattern.compile("^[a-z][a-z0-9-]{0,49}$");
    private final ExecutorService executorService;
    private final ReleaseService releaseService;
    private final ItemService itemService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ExecutorController(ExecutorService executorService, ReleaseService releaseService, ItemService itemService
            , ApplicationEventPublisher applicationEventPublisher) {
        this.executorService = executorService;
        this.releaseService = releaseService;
        this.itemService = itemService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PreAuthorize(value = "@permissionValidator.hasCreateOrDeleteExecutorPermission(#namespaceName, #appId, #operator)")
    @PostMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}")
    public ApiResponse<ExecutorResponse> create(@PathVariable("namespaceName") String namespaceName,
                                                @PathVariable("appId") String appId,
                                                @PathVariable("executorName") String executorName,
                                                @RequestAttribute(CLAIM_USER_NAME) String operator) {
        // 命名检查
        Matcher matcher = EXECUTOR_PATTERN.matcher(executorName);
        if (!matcher.matches()) {
            throw new BusinessException(AdminErrorCodeEnum.EXECUTOR_INVALID);
        }

        Executor managedEntity = executorService.findOne(namespaceName, appId, executorName);
        if (managedEntity != null) {
            throw new BusinessException(AdminErrorCodeEnum.EXECUTOR_EXIST);
        }

        Executor executor = executorService.save(namespaceName, appId, executorName, operator);

        applicationEventPublisher.publishEvent(new PermissionInitEvent(executor, RoleTypeEnum.EXECUTOR_MANAGER));

        ExecutorResponse executorResponse = LocalBeanUtils.transform(ExecutorResponse.class, executor);
        // 创建后 Item为默认修改状态，待发布
        executorResponse.setItemModified(true);

        return ApiResponse.success(executorResponse);
    }

    @PreAuthorize(value = "@permissionValidator.hasCreateOrDeleteExecutorPermission(#namespaceName, #appId, #operator)")
    @DeleteMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}")
    public ApiResponse<String> delete(@PathVariable("namespaceName") String namespaceName,
                                      @PathVariable("appId") String appId,
                                      @PathVariable("executorName") String executorName,
                                      @RequestAttribute(CLAIM_USER_NAME) String operator) {
        Executor entity = executorService.findOne(namespaceName, appId, executorName);
        if (entity == null) {
            throw new NotFoundException(
                    String.format("executor not found for %s %s %s", namespaceName, appId, executorName));
        }

        executorService.deleteExecutor(entity, operator);

        return ApiResponse.success("删除成功");
    }

    @GetMapping("/namespaces/{namespaceName}/apps/{appId}/executors")
    public ApiResponse<PageResult<ExecutorResponse>> find(@PathVariable("namespaceName") String namespaceName,
                                                          @PathVariable("appId") String appId,
                                                          PageHelperRequest pageHelperRequest) {
        PageResult<Executor> executorsPage = executorService.findExecutors(namespaceName, appId, pageHelperRequest);
        PageResult<ExecutorResponse> executorResponsePage = transform2PageResult(executorsPage);

        executorResponsePage.getContent().forEach(i -> {
            Item item = itemService.findOneByExecutorId(i.getId());
            if (item == null) {
                return;
            }
            Release release = releaseService.findLatestActiveRelease(i.getNamespaceName(), i.getAppId(),
                    i.getExecutorName());
            if (release == null || !item.getConfiguration().equals(release.getConfigurations())) {
                i.setItemModified(true);
            }
        });

        return ApiResponse.success(executorResponsePage);
    }

    @GetMapping("/executors/{executorId}")
    public ApiResponse<ExecutorResponse> get(@PathVariable("executorId") Long executorId) {
        Executor executor = executorService.findOne(executorId);
        if (executor == null) {
            throw new NotFoundException(String.format("executor not found for %s", executorId));
        }
        ExecutorResponse executorResponse = LocalBeanUtils.transform(ExecutorResponse.class, executor);

        Item item = itemService.findOneByExecutorId(executor.getId());
        if (item == null) {
            return ApiResponse.success(executorResponse);
        }
        Release release = releaseService.findLatestActiveRelease(executor.getNamespaceName(), executor.getAppId(),
                executor.getExecutorName());
        if (release == null || !item.getConfiguration().equals(release.getConfigurations())) {
            executorResponse.setItemModified(true);
        }

        return ApiResponse.success(executorResponse);
    }

    @GetMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}")
    public ApiResponse<ExecutorResponse> get(@PathVariable("namespaceName") String namespaceName,
                                             @PathVariable("appId") String appId,
                                             @PathVariable("executorName") String executorName) {
        Executor executor = executorService.findOne(namespaceName, appId, executorName);
        if (executor == null) {
            throw new NotFoundException(
                    String.format("executor not found for %s %s %s", namespaceName, appId, executorName));
        }
        ExecutorResponse executorResponse = LocalBeanUtils.transform(ExecutorResponse.class, executor);

        Item item = itemService.findOneByExecutorId(executor.getId());
        if (item == null) {
            return ApiResponse.success(executorResponse);
        }
        Release release = releaseService.findLatestActiveRelease(executor.getNamespaceName(), executor.getAppId(),
                executor.getExecutorName());
        if (release == null || !item.getConfiguration().equals(release.getConfigurations())) {
            executorResponse.setItemModified(true);
        }

        return ApiResponse.success(executorResponse);
    }

    private PageResult<ExecutorResponse> transform2PageResult(PageResult<Executor> executorPageResult) {
        List<Executor> executors = executorPageResult.getContent();
        List<ExecutorResponse> executorResponses = new ArrayList<>(executors.size());
        for (Executor executor : executors) {
            executorResponses.add(LocalBeanUtils.transform(ExecutorResponse.class, executor));
        }

        return new PageResult<>(executorResponses, executorPageResult.getTotal(), executorPageResult.getPageNum(),
                executorPageResult.getPageSize());
    }
}
