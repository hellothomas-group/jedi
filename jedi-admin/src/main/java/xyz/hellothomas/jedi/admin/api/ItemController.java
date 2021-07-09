package xyz.hellothomas.jedi.admin.api;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import xyz.hellothomas.jedi.admin.api.dto.ItemResponse;
import xyz.hellothomas.jedi.admin.application.ItemService;
import xyz.hellothomas.jedi.admin.domain.Item;
import xyz.hellothomas.jedi.admin.infrastructure.annotation.UserLoginToken;
import xyz.hellothomas.jedi.biz.common.utils.LocalBeanUtils;
import xyz.hellothomas.jedi.biz.infrastructure.exception.BadRequestException;
import xyz.hellothomas.jedi.biz.infrastructure.exception.NotFoundException;
import xyz.hellothomas.jedi.core.dto.ApiResponse;
import xyz.hellothomas.jedi.core.utils.JsonUtil;

import java.time.LocalDateTime;

import static xyz.hellothomas.jedi.admin.common.utils.JwtUtil.CLAIM_USER_NAME;

@UserLoginToken
@Api(value = "item", tags = "item")
@RestController
public class ItemController {

    private final ItemService itemService;

    public ItemController(final ItemService itemService) {
        this.itemService = itemService;
    }

    //    @PreAcquireNamespaceLock
    @PostMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/items")
    public ApiResponse<ItemResponse> create(@PathVariable("namespaceName") String namespaceName,
                                            @PathVariable("appId") String appId,
                                            @PathVariable("executorName") String executorName,
                                            @RequestParam("configuration") String configuration,
                                            @RequestParam(name = "comment", required = false) String comment,
                                            @RequestAttribute(CLAIM_USER_NAME) String operator) {
        Item managedEntity = itemService.findOne(namespaceName, appId, executorName);
        if (managedEntity != null) {
            throw new BadRequestException("item already exists");
        }
        Item entity = itemService.save(namespaceName, appId, executorName, configuration, comment, operator);
        return ApiResponse.success(LocalBeanUtils.transform(ItemResponse.class, entity));
    }

    //    @PreAcquireNamespaceLock
    @PutMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/items/{itemId}")
    public ApiResponse<ItemResponse> update(@PathVariable("namespaceName") String namespaceName,
                                            @PathVariable("appId") String appId,
                                            @PathVariable("executorName") String executorName,
                                            @PathVariable("itemId") long itemId,
                                            @RequestParam("configuration") String configuration,
                                            @RequestParam(name = "comment", required = false) String comment,
                                            @RequestAttribute(CLAIM_USER_NAME) String operator) {
        Item managedEntity = itemService.findOne(itemId);
        if (managedEntity == null || managedEntity.getIsDeleted()) {
            throw new BadRequestException("item not exist");
        }

        managedEntity.setComment(comment);
        managedEntity.setConfiguration(configuration);
        managedEntity.setDataChangeLastModifiedBy(operator);
        managedEntity.setDataChangeLastModifiedTime(LocalDateTime.now());
        itemService.update(managedEntity);

        return ApiResponse.success(LocalBeanUtils.transform(ItemResponse.class, managedEntity));
    }

    @PutMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/items")
    public ApiResponse<ItemResponse> update(@PathVariable("namespaceName") String namespaceName,
                                            @PathVariable("appId") String appId,
                                            @PathVariable("executorName") String executorName,
                                            @RequestParam("configuration") String configuration,
                                            @RequestParam(name = "comment", required = false) String comment,
                                            @RequestAttribute(CLAIM_USER_NAME) String operator) {
        if (!JsonUtil.isJSONValid(configuration)) {
            throw new BadRequestException("configuration invalid, must be json");
        }

        Item managedEntity = itemService.findOne(namespaceName, appId, executorName);
        if (managedEntity == null || managedEntity.getIsDeleted()) {
            throw new BadRequestException("item not exist");
        }

        managedEntity.setComment(comment);
        managedEntity.setConfiguration(configuration);
        managedEntity.setDataChangeLastModifiedBy(operator);
        managedEntity.setDataChangeLastModifiedTime(LocalDateTime.now());
        itemService.update(managedEntity);

        return ApiResponse.success(LocalBeanUtils.transform(ItemResponse.class, managedEntity));
    }

    //    @PreAcquireNamespaceLock
    @DeleteMapping("/items/{itemId}")
    public ApiResponse<String> delete(@PathVariable("itemId") long itemId,
                                      @RequestAttribute(CLAIM_USER_NAME) String operator) {
        Item entity = itemService.findOne(itemId);
        if (entity == null) {
            throw new NotFoundException("item not found for itemId " + itemId);
        }
        itemService.delete(entity.getId(), operator);

        return ApiResponse.success("删除成功");
    }

    @GetMapping("/items/{itemId}")
    public ApiResponse<ItemResponse> get(@PathVariable("itemId") long itemId) {
        Item item = itemService.findOne(itemId);
        if (item == null) {
            throw new NotFoundException("item not found for itemId " + itemId);
        }
        return ApiResponse.success(LocalBeanUtils.transform(ItemResponse.class, item));
    }

    @GetMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/items")
    public ApiResponse<ItemResponse> get(@PathVariable("namespaceName") String namespaceName,
                                         @PathVariable("appId") String appId,
                                         @PathVariable("executorName") String executorName) {
        Item item = itemService.findOne(namespaceName, appId, executorName);
        if (item == null) {
            throw new NotFoundException(
                    String.format("item not found for %s %s %s", namespaceName, appId, executorName));
        }
        return ApiResponse.success(LocalBeanUtils.transform(ItemResponse.class, item));
    }
}
