package xyz.hellothomas.jedi.admin.application;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.hellothomas.jedi.admin.api.dto.PageHelperRequest;
import xyz.hellothomas.jedi.admin.api.dto.PageResult;
import xyz.hellothomas.jedi.admin.domain.Audit;
import xyz.hellothomas.jedi.admin.domain.ReleaseHistory;
import xyz.hellothomas.jedi.admin.domain.ReleaseHistoryExample;
import xyz.hellothomas.jedi.admin.infrastructure.mapper.ReleaseHistoryMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static xyz.hellothomas.jedi.biz.common.constants.Constants.DEFAULT_PAGE_SIZE;

@Service
public class ReleaseHistoryService {
    private Gson gson = new Gson();

    private final ReleaseHistoryMapper releaseHistoryMapper;
    private final AuditService auditService;

    public ReleaseHistoryService(
            final ReleaseHistoryMapper releaseHistoryMapper,
            final AuditService auditService) {
        this.releaseHistoryMapper = releaseHistoryMapper;
        this.auditService = auditService;
    }


    public PageResult<ReleaseHistory> findReleaseHistoriesByNamespace(String namespaceName, String appId,
                                                                      String executorName,
                                                                      PageHelperRequest pageHelperRequest) {
        ReleaseHistoryExample releaseHistoryExample = new ReleaseHistoryExample();
        releaseHistoryExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdEqualTo(appId)
                .andExecutorNameEqualTo(executorName);
        releaseHistoryExample.setOrderByClause("id desc");

        int pageSize = pageHelperRequest.getPageSize();
        int pageNum = pageHelperRequest.getPageNum();
        pageSize = (pageSize <= 0) ? DEFAULT_PAGE_SIZE : pageSize;
        PageHelper.startPage(pageNum, pageSize);

        List<ReleaseHistory> releaseHistories = releaseHistoryMapper.selectByExample(releaseHistoryExample);
        PageInfo<ReleaseHistory> pageInfo = new PageInfo<>(releaseHistories);

        return PageResult.<ReleaseHistory>builder()
                .content(pageInfo.getList())
                .total(pageInfo.getTotal())
                .pageNum(pageInfo.getPageNum())
                .pageSize(pageInfo.getPageSize())
                .build();
    }

    public PageResult<ReleaseHistory> findByReleaseIdAndOperation(long releaseId, int operation,
                                                                  PageHelperRequest pageHelperRequest) {
        ReleaseHistoryExample releaseHistoryExample = new ReleaseHistoryExample();
        releaseHistoryExample.createCriteria().andReleaseIdEqualTo(releaseId)
                .andOperationEqualTo(operation);
        releaseHistoryExample.setOrderByClause("id desc");

        int pageSize = pageHelperRequest.getPageSize();
        int pageNum = pageHelperRequest.getPageNum();
        pageSize = (pageSize <= 0) ? DEFAULT_PAGE_SIZE : pageSize;
        PageHelper.startPage(pageNum, pageSize);

        List<ReleaseHistory> releaseHistories = releaseHistoryMapper.selectByExample(releaseHistoryExample);
        PageInfo<ReleaseHistory> pageInfo = new PageInfo<>(releaseHistories);

        return PageResult.<ReleaseHistory>builder()
                .content(pageInfo.getList())
                .total(pageInfo.getTotal())
                .pageNum(pageInfo.getPageNum())
                .pageSize(pageInfo.getPageSize())
                .build();
    }

    public PageResult<ReleaseHistory> findByPreviousReleaseIdAndOperation(long previousReleaseId, int operation,
                                                                          PageHelperRequest pageHelperRequest) {
        ReleaseHistoryExample releaseHistoryExample = new ReleaseHistoryExample();
        releaseHistoryExample.createCriteria().andPreviousReleaseIdEqualTo(previousReleaseId)
                .andOperationEqualTo(operation);
        releaseHistoryExample.setOrderByClause("id desc");

        int pageSize = pageHelperRequest.getPageSize();
        int pageNum = pageHelperRequest.getPageNum();
        pageSize = (pageSize <= 0) ? DEFAULT_PAGE_SIZE : pageSize;
        PageHelper.startPage(pageNum, pageSize);

        List<ReleaseHistory> releaseHistories = releaseHistoryMapper.selectByExample(releaseHistoryExample);
        PageInfo<ReleaseHistory> pageInfo = new PageInfo<>(releaseHistories);

        return PageResult.<ReleaseHistory>builder()
                .content(pageInfo.getList())
                .total(pageInfo.getTotal())
                .pageNum(pageInfo.getPageNum())
                .pageSize(pageInfo.getPageSize())
                .build();
    }

    public PageResult<ReleaseHistory> findByReleaseIdAndOperationInOrderByIdDesc(long releaseId,
                                                                                 Set<Integer> operations,
                                                                                 PageHelperRequest pageHelperRequest) {
        ReleaseHistoryExample releaseHistoryExample = new ReleaseHistoryExample();
        releaseHistoryExample.createCriteria().andReleaseIdEqualTo(releaseId)
                .andOperationIn(Lists.newArrayList(operations));
        releaseHistoryExample.setOrderByClause("id desc");

        int pageSize = pageHelperRequest.getPageSize();
        int pageNum = pageHelperRequest.getPageNum();
        pageSize = (pageSize <= 0) ? DEFAULT_PAGE_SIZE : pageSize;
        PageHelper.startPage(pageNum, pageSize);

        List<ReleaseHistory> releaseHistories = releaseHistoryMapper.selectByExample(releaseHistoryExample);
        PageInfo<ReleaseHistory> pageInfo = new PageInfo<>(releaseHistories);

        return PageResult.<ReleaseHistory>builder()
                .content(pageInfo.getList())
                .total(pageInfo.getTotal())
                .pageNum(pageInfo.getPageNum())
                .pageSize(pageInfo.getPageSize())
                .build();
    }

    @Transactional
    public ReleaseHistory createReleaseHistory(String namespaceName, String appId, String executorName,
                                               long releaseId, long previousReleaseId, int operation,
                                               Map<String, Object> operationContext, String operator) {
        ReleaseHistory releaseHistory = new ReleaseHistory();
        releaseHistory.setNamespaceName(namespaceName);
        releaseHistory.setAppId(appId);
        releaseHistory.setExecutorName(executorName);
        releaseHistory.setReleaseId(releaseId);
        releaseHistory.setPreviousReleaseId(previousReleaseId);
        releaseHistory.setOperation(operation);
        if (operationContext == null) {
            releaseHistory.setOperationContext("{}"); //default empty object
        } else {
            releaseHistory.setOperationContext(gson.toJson(operationContext));
        }
        LocalDateTime currentDateTime = LocalDateTime.now();
        releaseHistory.setCreateTime(currentDateTime);
        releaseHistory.setCreateUser(operator);
        releaseHistory.setUpdateTime(currentDateTime);
        releaseHistory.setUpdateUser(operator);

        releaseHistoryMapper.insertSelective(releaseHistory);

        auditService.audit(ReleaseHistory.class.getSimpleName(), releaseHistory.getId(),
                Audit.OP.INSERT, releaseHistory.getCreateUser());

        return releaseHistory;
    }

    @Transactional
    public int batchDelete(String namespaceName, String appId, String executorName, String operator) {
        ReleaseHistoryExample releaseHistoryExample = new ReleaseHistoryExample();
        releaseHistoryExample.createCriteria().andNamespaceNameEqualTo(namespaceName)
                .andAppIdEqualTo(appId)
                .andExecutorNameEqualTo(executorName)
                .andIsDeletedEqualTo(false);
        ReleaseHistory releaseHistory = new ReleaseHistory();
        releaseHistory.setIsDeleted(true);
        releaseHistory.setUpdateTime(LocalDateTime.now());
        releaseHistory.setUpdateUser(operator);
        return releaseHistoryMapper.updateByExampleSelective(releaseHistory, releaseHistoryExample);
    }
}
