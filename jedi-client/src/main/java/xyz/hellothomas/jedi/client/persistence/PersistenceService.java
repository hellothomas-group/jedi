package xyz.hellothomas.jedi.client.persistence;

import xyz.hellothomas.jedi.client.model.JediTaskExecution;
import xyz.hellothomas.jedi.core.internals.executor.TaskProperty;

import java.time.LocalDateTime;
import java.util.List;

public interface PersistenceService {
    int insertTaskExecution(TaskProperty taskProperty);

    int updateTaskExecution(TaskProperty taskProperty);

    int deleteTaskExecution(TaskProperty taskProperty);

    JediTaskExecution queryTaskExecutionById(String taskId, String dataSourceName);

    long queryCountByStatusAndRecoverable(String machineId, int status, boolean recoverable, LocalDateTime appInitTime,
                                          String dataSourceName);

    List<JediTaskExecution> queryPageByStatusAndRecoverable(String machineId, int status, boolean recoverable,
                                                            LocalDateTime appInitTime, int pageNum, int pageSize,
                                                            String dataSourceName);
}
