package xyz.hellothomas.jedi.client.persistence;

import xyz.hellothomas.jedi.client.model.JediTaskExecution;
import xyz.hellothomas.jedi.core.internals.executor.TaskProperty;

public interface PersistenceService {
    int insertTaskExecution(TaskProperty taskProperty);

    int updateTaskExecution(TaskProperty taskProperty);

    int deleteTaskExecution(TaskProperty taskProperty);

    JediTaskExecution queryTaskExecutionById(String taskId, String dataSourceName);
}
