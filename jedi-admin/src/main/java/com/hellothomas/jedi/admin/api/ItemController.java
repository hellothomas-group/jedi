package com.hellothomas.jedi.admin.api;

import com.hellothomas.jedi.admin.api.dto.ItemResponse;
import com.hellothomas.jedi.admin.application.ItemService;
import com.hellothomas.jedi.admin.domain.Item;
import com.hellothomas.jedi.biz.common.utils.LocalBeanUtils;
import com.hellothomas.jedi.biz.infrastructure.exception.BadRequestException;
import com.hellothomas.jedi.biz.infrastructure.exception.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class ItemController {

    private final ItemService itemService;

    public ItemController(final ItemService itemService) {
        this.itemService = itemService;
    }

    //    @PreAcquireNamespaceLock
    @PostMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/items")
    public ItemResponse create(@PathVariable("namespaceName") String namespaceName,
                               @PathVariable("appId") String appId,
                               @PathVariable("executorName") String executorName,
                               @RequestParam("configuration") String configuration,
                               @RequestParam(name = "comment", required = false) String comment,
                               @RequestParam("operator") String operator) {
        Item managedEntity = itemService.findOne(namespaceName, appId, executorName);
        if (managedEntity != null) {
            throw new BadRequestException("item already exists");
        }
        Item entity = itemService.save(namespaceName, appId, executorName, configuration, comment, operator);
        return LocalBeanUtils.transform(ItemResponse.class, entity);
    }

    //    @PreAcquireNamespaceLock
    @PutMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/items/{itemId}")
    public ItemResponse update(@PathVariable("namespaceName") String namespaceName,
                               @PathVariable("appId") String appId,
                               @PathVariable("executorName") String executorName,
                               @PathVariable("itemId") long itemId,
                               @RequestParam("configuration") String configuration,
                               @RequestParam(name = "comment", required = false) String comment,
                               @RequestParam("operator") String operator) {
        Item managedEntity = itemService.findOne(itemId);
        if (managedEntity == null || managedEntity.getIsDeleted()) {
            throw new BadRequestException("item not exist");
        }

        managedEntity.setComment(comment);
        managedEntity.setConfiguration(configuration);
        managedEntity.setDataChangeLastModifiedBy(operator);
        managedEntity.setDataChangeLastModifiedTime(LocalDateTime.now());
        itemService.update(managedEntity);

        return LocalBeanUtils.transform(ItemResponse.class, managedEntity);
    }

    //    @PreAcquireNamespaceLock
    @DeleteMapping("/items/{itemId}")
    public void delete(@PathVariable("itemId") long itemId, @RequestParam String operator) {
        Item entity = itemService.findOne(itemId);
        if (entity == null) {
            throw new NotFoundException("item not found for itemId " + itemId);
        }
        itemService.delete(entity.getId(), operator);
    }

    @GetMapping("/items/{itemId}")
    public ItemResponse get(@PathVariable("itemId") long itemId) {
        Item item = itemService.findOne(itemId);
        if (item == null) {
            throw new NotFoundException("item not found for itemId " + itemId);
        }
        return LocalBeanUtils.transform(ItemResponse.class, item);
    }

    @GetMapping("/namespaces/{namespaceName}/apps/{appId}/executors/{executorName}/items")
    public ItemResponse get(@PathVariable("namespaceName") String namespaceName,
                            @PathVariable("appId") String appId,
                            @PathVariable("executorName") String executorName) {
        Item item = itemService.findOne(namespaceName, appId, executorName);
        if (item == null) {
            throw new NotFoundException(
                    String.format("item not found for %s %s %s", namespaceName, appId, executorName));
        }
        return LocalBeanUtils.transform(ItemResponse.class, item);
    }
}
