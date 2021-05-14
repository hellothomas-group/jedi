package com.hellothomas.jedi.admin.api;

import com.hellothomas.jedi.admin.api.dto.ExecutorResponse;
import com.hellothomas.jedi.admin.application.ExecutorService;
import com.hellothomas.jedi.admin.domain.Executor;
import com.hellothomas.jedi.biz.common.utils.LocalBeanUtils;
import com.hellothomas.jedi.biz.infrastructure.exception.BadRequestException;
import com.hellothomas.jedi.biz.infrastructure.exception.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ExecutorController {

    private final ExecutorService executorService;

    public ExecutorController(ExecutorService executorService) {
        this.executorService = executorService;
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

        return LocalBeanUtils.transform(ExecutorResponse.class, executor);
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
    public List<ExecutorResponse> find(@PathVariable("namespaceName") String namespaceName,
                                       @PathVariable("appId") String appId) {
        List<Executor> groups = executorService.findExecutors(namespaceName, appId);
        return LocalBeanUtils.batchTransform(ExecutorResponse.class, groups);
    }

    @GetMapping("/executors/{executorId}")
    public ExecutorResponse get(@PathVariable("executorId") Long executorId) {
        Executor executor = executorService.findOne(executorId);
        if (executor == null) {
            throw new NotFoundException(String.format("executor not found for %s", executorId));
        }
        return LocalBeanUtils.transform(ExecutorResponse.class, executor);
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
        return LocalBeanUtils.transform(ExecutorResponse.class, executor);
    }
}
