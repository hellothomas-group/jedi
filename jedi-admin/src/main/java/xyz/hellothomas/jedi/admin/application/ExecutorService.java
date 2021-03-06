package xyz.hellothomas.jedi.admin.application;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.hellothomas.jedi.admin.api.dto.PageHelperRequest;
import xyz.hellothomas.jedi.admin.api.dto.PageResult;
import xyz.hellothomas.jedi.admin.application.message.MessageSender;
import xyz.hellothomas.jedi.admin.domain.Audit;
import xyz.hellothomas.jedi.admin.domain.Executor;
import xyz.hellothomas.jedi.admin.domain.ExecutorExample;
import xyz.hellothomas.jedi.admin.infrastructure.mapper.ExecutorMapper;
import xyz.hellothomas.jedi.biz.common.utils.ReleaseMessageKeyGenerator;
import xyz.hellothomas.jedi.biz.infrastructure.exception.ServiceException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static xyz.hellothomas.jedi.biz.common.constants.Constants.DEFAULT_PAGE_SIZE;
import static xyz.hellothomas.jedi.biz.common.constants.Constants.JEDI_RELEASE_TOPIC;

@Slf4j
@Service
public class ExecutorService {
    private static final String ITEM_DEFAULT_CONFIGURATION = "{\"corePoolSize\":10,\"maxPoolSize\":20," +
            "\"queueCapacity\":1000,\"keepAliveSeconds\":60,\"tickerCycle\":5000," +
            "\"allowCoreThreadTimeOut\":false}";
    private static final String ITEM_DEFAULT_COMMENT = "createExecutorAutoGenerated";

    private final ExecutorMapper executorMapper;
    private final AuditService auditService;
    private final ItemService itemService;
    private final ReleaseService releaseService;
    private final ReleaseHistoryService releaseHistoryService;
    private final ExecutorLockService executorLockService;
    private final InstanceService instanceService;
    private final MessageSender messageSender;

    public ExecutorService(
            final ReleaseHistoryService releaseHistoryService,
            final ExecutorMapper executorMapper,
            final AuditService auditService,
            final MessageSender messageSender,
            final @Lazy ItemService itemService,
            final @Lazy ReleaseService releaseService,
            final ExecutorLockService executorLockService,
            final InstanceService instanceService) {
        this.releaseHistoryService = releaseHistoryService;
        this.executorMapper = executorMapper;
        this.auditService = auditService;
        this.messageSender = messageSender;
        this.itemService = itemService;
        this.releaseService = releaseService;
        this.executorLockService = executorLockService;
        this.instanceService = instanceService;
    }

    public Executor findOne(Long executorId) {
        Executor executor = executorMapper.selectByPrimaryKey(executorId);

        if (executor != null && executor.getIsDeleted()) {
            return null;
        }

        return executor;
    }

    public Executor findOneIncludingIsDeleted(Long executorId) {
        return executorMapper.selectByPrimaryKey(executorId);
    }

    public Executor findOne(String namespaceName, String appId, String executorName) {
        ExecutorExample executorExample = new ExecutorExample();
        executorExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdEqualTo(appId)
                .andExecutorNameEqualTo(executorName)
                .andIsDeletedEqualTo(false);
        List<Executor> result = executorMapper.selectByExample(executorExample);
        return result.isEmpty() ? null : result.get(0);
    }

    public List<Executor> findExecutors(String namespace, String appId) {
        ExecutorExample executorExample = new ExecutorExample();
        executorExample.createCriteria().andNamespaceNameEqualTo(namespace)
                .andAppIdEqualTo(appId)
                .andIsDeletedEqualTo(false);
        return executorMapper.selectByExample(executorExample);
    }

    public PageResult<Executor> findExecutors(String namespace, String appId, PageHelperRequest pageHelperRequest) {
        ExecutorExample executorExample = new ExecutorExample();
        executorExample.createCriteria().andNamespaceNameEqualTo(namespace)
                .andAppIdEqualTo(appId)
                .andIsDeletedEqualTo(false);
        executorExample.setOrderByClause("executor_name");

        int pageSize = pageHelperRequest.getPageSize();
        int pageNum = pageHelperRequest.getPageNum();
        pageSize = (pageSize <= 0) ? DEFAULT_PAGE_SIZE : pageSize;
        PageHelper.startPage(pageNum, pageSize);


        List<Executor> executors = executorMapper.selectByExample(executorExample);
        PageInfo<Executor> pageInfo = new PageInfo<>(executors);

        return PageResult.<Executor>builder()
                .content(pageInfo.getList())
                .total(pageInfo.getTotal())
                .pageNum(pageInfo.getPageNum())
                .pageSize(pageInfo.getPageSize())
                .build();
    }

    public boolean isNamespaceUnique(String namespace, String appId, String executor) {
        Objects.requireNonNull(namespace, "Namespace must not be null");
        Objects.requireNonNull(appId, "AppId must not be null");
        Objects.requireNonNull(executor, "Executor must not be null");

        ExecutorExample executorExample = new ExecutorExample();
        executorExample.createCriteria().andNamespaceNameEqualTo(namespace)
                .andAppIdEqualTo(appId)
                .andExecutorNameEqualTo(executor);
        return executorMapper.countByExample(executorExample) == 0;
    }

    @Transactional
    public void deleteByNamespaceAndAppId(String namespaceName, String appId, String operator) {

        List<Executor> toDeleteExecutors = findExecutors(namespaceName, appId);

        for (Executor executor : toDeleteExecutors) {

            deleteExecutor(executor, operator);

        }
    }

    @Transactional
    public Executor deleteExecutor(Executor executor, String operator) {
        String namespaceName = executor.getNamespaceName();
        String appId = executor.getAppId();
        String executorName = executor.getExecutorName();

        itemService.deleteByExecutorId(executor.getId(), operator);

        releaseService.batchDelete(namespaceName, appId, executorName, operator);

        releaseHistoryService.batchDelete(namespaceName, appId, executorName, operator);

        instanceService.batchDeleteInstanceConfig(namespaceName, appId, executorName);

        executorLockService.unlock(executor.getId());

        executor.setIsDeleted(true);
        executor.setUpdateUser(operator);
        executor.setUpdateTime(LocalDateTime.now());

        auditService.audit(Executor.class.getSimpleName(), executor.getId(), Audit.OP.DELETE, operator);

        executorMapper.updateByPrimaryKey(executor);

        //Publish release message to do some clean up in config service, such as updating the cache
        messageSender.sendMessage(ReleaseMessageKeyGenerator.generate(namespaceName, appId, executorName),
                JEDI_RELEASE_TOPIC);

        return executor;
    }

    @Transactional
    public Executor save(String namespace, String appId, String executorName, String operator) {
        if (!isNamespaceUnique(namespace, appId, executorName)) {
            throw new ServiceException("namespace not unique");
        }

        LocalDateTime currentDateTime = LocalDateTime.now();
        Executor executor = new Executor();
        executor.setNamespaceName(namespace);
        executor.setAppId(appId);
        executor.setExecutorName(executorName);
        executor.setIsDeleted(false);
        executor.setCreateTime(currentDateTime);
        executor.setCreateUser(operator);
        executor.setUpdateTime(currentDateTime);
        executor.setUpdateUser(operator);

        executorMapper.insertSelective(executor);

        log.debug("inserted executor:{}", executor);

        itemService.saveByExecutor(executor, ITEM_DEFAULT_CONFIGURATION, ITEM_DEFAULT_COMMENT, operator);

        auditService.audit(Executor.class.getSimpleName(), executor.getId(), Audit.OP.INSERT,
                executor.getCreateUser());

        return executor;
    }

    @Transactional
    public int batchDelete(String namespaceName, String appId, String operator) {
        ExecutorExample executorExample = new ExecutorExample();

        executorExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdEqualTo(appId)
                .andIsDeletedEqualTo(false);
        Executor executor = new Executor();
        executor.setIsDeleted(true);
        executor.setUpdateTime(LocalDateTime.now());
        executor.setUpdateUser(operator);
        return executorMapper.updateByExampleSelective(executor, executorExample);
    }
}
