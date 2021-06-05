package xyz.hellothomas.jedi.admin.application;


import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.hellothomas.jedi.admin.domain.Audit;
import xyz.hellothomas.jedi.admin.domain.Executor;
import xyz.hellothomas.jedi.admin.domain.Item;
import xyz.hellothomas.jedi.admin.domain.ItemExample;
import xyz.hellothomas.jedi.admin.infrastructure.mapper.ItemMapper;
import xyz.hellothomas.jedi.biz.infrastructure.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ItemService {

    private final ItemMapper itemMapper;
    private final ExecutorService executorService;
    private final AuditService auditService;

    public ItemService(
            final ItemMapper itemMapper,
            final @Lazy ExecutorService executorService,
            final AuditService auditService) {
        this.itemMapper = itemMapper;
        this.executorService = executorService;
        this.auditService = auditService;
    }

    @Transactional
    public Item delete(long id, String operator) {
        Item item = itemMapper.selectByPrimaryKey(id);
        if (item == null) {
            throw new IllegalArgumentException("item not exist. ID:" + id);
        }

        item.setIsDeleted(true);
        item.setDataChangeLastModifiedBy(operator);
        item.setDataChangeLastModifiedTime(LocalDateTime.now());
        itemMapper.updateByPrimaryKey(item);

        auditService.audit(Item.class.getSimpleName(), id, Audit.OP.DELETE, operator);
        return item;
    }

    @Transactional
    public int deleteByExecutorId(long executorId, String operator) {
        ItemExample itemExample = new ItemExample();
        itemExample.createCriteria().andExecutorIdEqualTo(executorId);

        Item item = new Item();
        item.setIsDeleted(true);
        item.setDataChangeLastModifiedBy(operator);
        item.setDataChangeLastModifiedTime(LocalDateTime.now());
        return itemMapper.updateByExampleSelective(item, itemExample);
    }

    public Item findOne(String namespaceName, String appId, String executorName) {
        Executor executor = executorService.findOne(namespaceName, appId, executorName);
        if (executor == null) {
            throw new NotFoundException(
                    String.format("executor not found for %s %s %s", namespaceName, appId, executorName));
        }

        return findOneByExecutorId(executor.getId());
    }

    public Item findOneIncludingIsDeleted(Long itemId) {
        return itemMapper.selectByPrimaryKey(itemId);
    }

    public Item findOne(Long itemId) {
        Item item = itemMapper.selectByPrimaryKey(itemId);

        if (item != null && item.getIsDeleted()) {
            return null;
        }

        return item;
    }

    public Item findOneByExecutorId(Long executorId) {
        ItemExample itemExample = new ItemExample();
        itemExample.createCriteria().andExecutorIdEqualTo(executorId)
                .andIsDeletedEqualTo(false);

        List<Item> items = itemMapper.selectByExample(itemExample);
        return items.size() == 0 ? null : items.get(0);
    }

    @Transactional
    public Item save(String namespaceName, String appId, String executorName, String configuration, String comment,
                     String operator) {
        Executor executor = executorService.findOne(namespaceName, appId, executorName);
        if (executor == null) {
            throw new NotFoundException(
                    String.format("executor not found for %s %s %s", namespaceName, appId, executorName));
        }

        Item item = new Item();
        item.setExecutorId(executor.getId());
        item.setConfiguration(configuration);
        item.setComment(comment);

        LocalDateTime currentDateTime = LocalDateTime.now();
        item.setDataChangeCreatedBy(operator);
        item.setDataChangeCreatedTime(currentDateTime);
        item.setDataChangeLastModifiedBy(operator);
        item.setDataChangeLastModifiedTime(currentDateTime);

        itemMapper.insertSelective(item);

        auditService.audit(Item.class.getSimpleName(), item.getId(), Audit.OP.INSERT,
                item.getDataChangeCreatedBy());

        return item;
    }

    @Transactional
    public Item saveByExecutor(Executor executor, String configuration, String comment, String operator) {
        Item item = new Item();
        item.setExecutorId(executor.getId());
        item.setConfiguration(configuration);
        item.setComment(comment);

        LocalDateTime currentDateTime = LocalDateTime.now();
        item.setDataChangeCreatedBy(operator);
        item.setDataChangeCreatedTime(currentDateTime);
        item.setDataChangeLastModifiedBy(operator);
        item.setDataChangeLastModifiedTime(currentDateTime);

        itemMapper.insertSelective(item);
        return item;
    }

    @Transactional
    public int update(Item item) {
        int updatedRows = itemMapper.updateByPrimaryKey(item);

        auditService.audit(Item.class.getSimpleName(), item.getId(), Audit.OP.UPDATE,
                item.getDataChangeLastModifiedBy());

        return updatedRows;
    }
}
