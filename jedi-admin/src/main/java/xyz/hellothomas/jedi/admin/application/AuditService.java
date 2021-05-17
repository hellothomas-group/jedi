package xyz.hellothomas.jedi.admin.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.hellothomas.jedi.admin.domain.Audit;
import xyz.hellothomas.jedi.admin.domain.AuditExample;
import xyz.hellothomas.jedi.admin.infrastructure.mapper.AuditMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditService {

    private final AuditMapper auditMapper;

    public AuditService(final AuditMapper auditMapper) {
        this.auditMapper = auditMapper;
    }

    public List<Audit> findByOwner(String owner) {
        AuditExample auditExample = new AuditExample();
        auditExample.createCriteria().andDataChangeCreatedByEqualTo(owner);
        auditExample.setOrderByClause("id");
        return auditMapper.selectByExample(auditExample);
    }

    public List<Audit> find(String owner, String entity, String op) {
        AuditExample auditExample = new AuditExample();
        auditExample.createCriteria().andDataChangeCreatedByEqualTo(owner)
                .andEntityNameEqualTo(entity)
                .andOperationNameEqualTo(op);
        auditExample.setOrderByClause("id");
        return auditMapper.selectByExample(auditExample);
    }

    @Transactional
    public void audit(String entityName, Long entityId, Audit.OP op, String owner) {
        Audit audit = new Audit();
        audit.setEntityName(entityName);
        audit.setEntityId(entityId);
        audit.setOperationName(op.name());
        audit.setDataChangeCreatedBy(owner);
        LocalDateTime currentDateTime = LocalDateTime.now();
        audit.setDataChangeCreatedTime(currentDateTime);
        audit.setDataChangeLastModifiedTime(currentDateTime);
        auditMapper.insertSelective(audit);
    }

    @Transactional
    public void audit(Audit audit) {
        auditMapper.insertSelective(audit);
    }
}
