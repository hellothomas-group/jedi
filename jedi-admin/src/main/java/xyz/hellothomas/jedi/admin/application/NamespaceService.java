package xyz.hellothomas.jedi.admin.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.hellothomas.jedi.biz.domain.monitor.App;
import xyz.hellothomas.jedi.admin.domain.Audit;
import xyz.hellothomas.jedi.admin.domain.Namespace;
import xyz.hellothomas.jedi.admin.domain.NamespaceExample;
import xyz.hellothomas.jedi.admin.infrastructure.mapper.NamespaceMapper;
import xyz.hellothomas.jedi.biz.infrastructure.exception.BadRequestException;
import xyz.hellothomas.jedi.biz.infrastructure.exception.ServiceException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class NamespaceService {

    private final NamespaceMapper namespaceMapper;
    private final AuditService auditService;
    private final AppService appService;

    public NamespaceService(NamespaceMapper namespaceMapper, AuditService auditService, AppService appService) {
        this.namespaceMapper = namespaceMapper;
        this.auditService = auditService;
        this.appService = appService;
    }

    public boolean isNamespaceUnique(String namespaceName) {
        Objects.requireNonNull(namespaceName, "namespace must not be null");
        NamespaceExample namespaceExample = new NamespaceExample();
        namespaceExample.createCriteria().andNameEqualTo(namespaceName);
        return namespaceMapper.countByExample(namespaceExample) == 0;
    }

    @Transactional
    public void delete(long id, String operator) {
        Namespace namespace = namespaceMapper.selectByPrimaryKey(id);
        if (namespace == null) {
            return;
        }

        namespace.setIsDeleted(true);
        namespace.setDataChangeLastModifiedBy(operator);
        namespace.setDataChangeLastModifiedTime(LocalDateTime.now());
        namespaceMapper.updateByPrimaryKey(namespace);

        auditService.audit(Namespace.class.getSimpleName(), id, Audit.OP.DELETE, operator);
    }

    public List<Namespace> findAll() {
        NamespaceExample namespaceExample = new NamespaceExample();
        namespaceExample.createCriteria().andIsDeletedEqualTo(false);

        return namespaceMapper.selectByExample(namespaceExample);
    }

    public List<Namespace> findByDescription(String description) {
        NamespaceExample namespaceExample = new NamespaceExample();
        namespaceExample.createCriteria().andDescriptionEqualTo(description);

        return namespaceMapper.selectByExample(namespaceExample);
    }

    public Namespace findOne(String namespaceName) {
        NamespaceExample namespaceExample = new NamespaceExample();
        namespaceExample.createCriteria().andNameEqualTo(namespaceName)
                .andIsDeletedEqualTo(false);

        List<Namespace> namespaces = namespaceMapper.selectByExample(namespaceExample);

        return namespaces.isEmpty() ? null : namespaces.get(0);
    }

    @Transactional
    public Namespace save(Namespace entity, String operator) {
        if (!isNamespaceUnique(entity.getName())) {
            throw new ServiceException("namespace not unique");
        }

        LocalDateTime currentDateTime = LocalDateTime.now();
        entity.setDataChangeCreatedTime(currentDateTime);
        entity.setDataChangeCreatedBy(operator);
        entity.setDataChangeLastModifiedTime(currentDateTime);
        entity.setDataChangeLastModifiedBy(operator);
        namespaceMapper.insertSelective(entity);

        auditService.audit(Namespace.class.getSimpleName(), entity.getId(), Audit.OP.INSERT,
                entity.getDataChangeCreatedBy());

        return entity;
    }

    @Transactional
    public void update(Namespace namespace, String operator) {
        String namespaceName = namespace.getName();
        Namespace managedNamespace = findOne(namespaceName);
        if (managedNamespace == null) {
            throw new BadRequestException(String.format("Namespace not exists. namespaceName= %s", namespaceName));
        }

        managedNamespace.setName(namespace.getName());
        managedNamespace.setDescription(namespace.getDescription());
        managedNamespace.setOrgId(namespace.getOrgId());
        managedNamespace.setOrgName(namespace.getOrgName());
        managedNamespace.setOwnerName(namespace.getOwnerName());
        managedNamespace.setOwnerEmail(namespace.getOwnerEmail());
        managedNamespace.setDataChangeLastModifiedBy(operator);
        managedNamespace.setDataChangeCreatedTime(LocalDateTime.now());

        namespaceMapper.updateByPrimaryKey(managedNamespace);

        auditService.audit(Namespace.class.getSimpleName(), managedNamespace.getId(), Audit.OP.UPDATE,
                managedNamespace.getDataChangeLastModifiedBy());
    }

    @Transactional
    public Namespace deleteNamespace(Namespace namespace, String operator) {
        List<App> apps = appService.findByNamespaceName(namespace.getName());

        for (App app : apps) {
            appService.deleteApp(app, operator);
        }

        namespace.setIsDeleted(true);
        namespace.setDataChangeLastModifiedBy(operator);
        namespace.setDataChangeLastModifiedTime(LocalDateTime.now());

        auditService.audit(Namespace.class.getSimpleName(), namespace.getId(), Audit.OP.DELETE, operator);

        namespaceMapper.updateByPrimaryKey(namespace);

        return namespace;
    }
}
