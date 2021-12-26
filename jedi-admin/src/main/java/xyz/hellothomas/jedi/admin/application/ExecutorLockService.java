package xyz.hellothomas.jedi.admin.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.hellothomas.jedi.admin.domain.ExecutorLock;
import xyz.hellothomas.jedi.admin.domain.ExecutorLockExample;
import xyz.hellothomas.jedi.admin.infrastructure.mapper.ExecutorLockMapper;

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
        entity.setCreateTime(currentDateTime);
        entity.setCreateUser(operator);
        entity.setUpdateTime(currentDateTime);
        entity.setUpdateUser(operator);

        executorLockMapper.insertSelective(entity);

        return entity;
    }

    @Transactional
    public void unlock(Long executorId) {
        executorLockMapper.deleteByPrimaryKey(executorId);
    }
}
