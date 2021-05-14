package com.hellothomas.jedi.admin.application;

import com.hellothomas.jedi.admin.domain.ExecutorLock;
import com.hellothomas.jedi.admin.domain.ExecutorLockExample;
import com.hellothomas.jedi.admin.infrastructure.mapper.ExecutorLockMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExecutorLockService {

    private final ExecutorLockMapper executorLockMapper;

    public ExecutorLockService(ExecutorLockMapper executorLockMapper) {
        this.executorLockMapper = executorLockMapper;
    }

    public ExecutorLock findLock(Long executorId) {
        ExecutorLockExample executorLockExample = new ExecutorLockExample();
        executorLockExample.createCriteria().andExecutorIdEqualTo(executorId);
        List<ExecutorLock> result = executorLockMapper.selectByExample(executorLockExample);
        return result.isEmpty() ? null : result.get(0);
    }


    @Transactional
    public ExecutorLock tryLock(Long executorId, String operator) {
        ExecutorLock entity = new ExecutorLock();
        entity.setExecutorId(executorId);
        LocalDateTime currentDateTime = LocalDateTime.now();
        entity.setDataChangeCreatedTime(currentDateTime);
        entity.setDataChangeCreatedBy(operator);
        entity.setDataChangeLastModifiedTime(currentDateTime);
        entity.setDataChangeLastModifiedBy(operator);

        executorLockMapper.insertSelective(entity);

        return entity;
    }

    @Transactional
    public void unlock(Long executorId) {
        executorLockMapper.deleteByPrimaryKey(executorId);
    }
}
