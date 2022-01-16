package xyz.hellothomas.jedi.client.persistence;

import xyz.hellothomas.jedi.client.model.JediTaskExecution;
import xyz.hellothomas.jedi.core.internals.executor.TaskProperty;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Thomas
 * @date 2021/12/19 22:34
 * @description
 * @version 1.0
 */
public class NullPersistentService implements PersistenceService {
    @Override
    public int insertTaskExecution(TaskProperty taskProperty) {
        throw new NullPointerException("No Available PersistenceServiceImpl");
    }

    @Override
    public int updateTaskExecution(TaskProperty taskProperty) {
        throw new NullPointerException("No Available PersistenceServiceImpl");
    }

    @Override
    public int deleteTaskExecution(TaskProperty taskProperty) {
        throw new NullPointerException("No Available PersistenceServiceImpl");
    }

    @Override
    public JediTaskExecution queryTaskExecutionById(String taskId, String dataSourceName) {
        throw new NullPointerException("No Available PersistenceServiceImpl");
    }

    @Override
    public long queryCountByStatusAndRecoverable(String machineId, int status, boolean recoverable, LocalDateTime appInitTime, String dataSourceName) {
        throw new NullPointerException("No Available PersistenceServiceImpl");
    }

    @Override
    public List<JediTaskExecution> queryPageByStatusAndRecoverable(String machineId, int status, boolean recoverable, LocalDateTime appInitTime, int pageNum,
                                                                   int pageSize,
                                                                   String dataSourceName) {
        throw new NullPointerException("No Available PersistenceServiceImpl");
    }
}
