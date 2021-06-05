package xyz.hellothomas.jedi.admin.api;

import org.springframework.web.bind.annotation.*;
import xyz.hellothomas.jedi.admin.api.dto.ExecutorResponse;
import xyz.hellothomas.jedi.admin.api.dto.PageHelperRequest;
import xyz.hellothomas.jedi.admin.api.dto.PageResult;
import xyz.hellothomas.jedi.admin.application.ExecutorService;
import xyz.hellothomas.jedi.admin.application.ItemService;
import xyz.hellothomas.jedi.admin.application.ReleaseService;
import xyz.hellothomas.jedi.admin.domain.Executor;
import xyz.hellothomas.jedi.admin.domain.Item;
import xyz.hellothomas.jedi.biz.common.utils.LocalBeanUtils;
import xyz.hellothomas.jedi.biz.domain.Release;
import xyz.hellothomas.jedi.biz.infrastructure.exception.BadRequestException;
import xyz.hellothomas.jedi.biz.infrastructure.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ExecutorController {

    private final ExecutorService executorService;
    private final ReleaseService releaseService;
    private final ItemService itemService;

    public ExecutorController(ExecutorService executorService, ReleaseService releaseService, ItemService itemService) {
        this.executorService = executorService;
        this.releaseService = releaseService;
        this.itemService = itemService;
    }

    @PostMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}")
    public ExecutorResponse create(@PathVariable("namespaceName") String namespaceName,
                                   @PathVariable("appId") String appId,
                                   @PathVariable("executorName") String executorName,
                                   @RequestParam("operator") String operator) {
        Executor managedEntity = executorService.findOne(namespaceName, appId, executorName);
        if (managedEntity != null) {
            throw new BadRequestException("executor already exist.");
        }

        Executor executor = executorService.save(namespaceName, appId, executorName, operator);

        ExecutorResponse executorResponse = LocalBeanUtils.transform(ExecutorResponse.class, executor);
        // 创建后 Item为默认修改状态，待发布
        executorResponse.setItemModified(true);

        return executorResponse;
    }

    @DeleteMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}")
    public void delete(@PathVariable("namespaceName") String namespaceName,
                       @PathVariable("appId") String appId,
                       @PathVariable("executorName") String executorName, @RequestParam("operator") String operator) {
        Executor entity = executorService.findOne(namespaceName, appId, executorName);
        if (entity == null) {
            throw new NotFoundException(
                    String.format("executor not found for %s %s %s", namespaceName, appId, executorName));
        }

        executorService.deleteExecutor(entity, operator);
    }

    @GetMapping("/namespaces/{namespaceName}/apps/{appId}/executors")
    public PageResult<ExecutorResponse> find(@PathVariable("namespaceName") String namespaceName,
                                             @PathVariable("appId") String appId, PageHelperRequest pageHelperRequest) {
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

        return executorResponsePage;
    }

    @GetMapping("/executors/{executorId}")
    public ExecutorResponse get(@PathVariable("executorId") Long executorId) {
        Executor executor = executorService.findOne(executorId);
        if (executor == null) {
            throw new NotFoundException(String.format("executor not found for %s", executorId));
        }
        ExecutorResponse executorResponse = LocalBeanUtils.transform(ExecutorResponse.class, executor);

        Item item = itemService.findOneByExecutorId(executor.getId());
        if (item == null) {
            return executorResponse;
        }
        Release release = releaseService.findLatestActiveRelease(executor.getNamespaceName(), executor.getAppId(),
                executor.getExecutorName());
        if (release == null || !item.getConfiguration().equals(release.getConfigurations())) {
            executorResponse.setItemModified(true);
        }

        return executorResponse;
    }

    @GetMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}")
    public ExecutorResponse get(@PathVariable("namespaceName") String namespaceName,
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
            return executorResponse;
        }
        Release release = releaseService.findLatestActiveRelease(executor.getNamespaceName(), executor.getAppId(),
                executor.getExecutorName());
        if (release == null || !item.getConfiguration().equals(release.getConfigurations())) {
            executorResponse.setItemModified(true);
        }

        return executorResponse;
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
