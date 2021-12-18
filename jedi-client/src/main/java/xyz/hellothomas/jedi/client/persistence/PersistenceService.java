package xyz.hellothomas.jedi.client.persistence;

import xyz.hellothomas.jedi.client.enums.TaskStatusEnum;

public interface PersistenceService {
    int insertTaskExecution(TaskPersistProperty taskPersistProperty);

    int updateTaskExecution(TaskPersistProperty taskPersistProperty, TaskStatusEnum taskStatusEnum, Exception e);

    int deleteTaskExecution(TaskPersistProperty taskPersistProperty);
}
