package xyz.hellothomas.jedi.client.persistence;

import xyz.hellothomas.jedi.client.enums.TaskStatusEnum;

import java.util.concurrent.Callable;

public class JediPersistentCallable<V> implements Callable<V> {

    private final Callable<V> callable;
    private final TaskPersistProperty taskPersistProperty;
    private final PersistenceService persistenceService;

    public JediPersistentCallable(Callable<V> callable, TaskPersistProperty taskPersistProperty,
                                  PersistenceService persistenceService) {
        this.callable = callable;
        this.taskPersistProperty = taskPersistProperty;
        this.persistenceService = persistenceService;
    }

    @Override
    public V call() throws Exception {
        try {
            V result = callable.call();
            persistenceService.deleteTaskExecution(taskPersistProperty);
            return result;
        } catch (Exception e) {
            persistenceService.updateTaskExecution(taskPersistProperty, TaskStatusEnum.FAIL, e);
            throw e;
        }
    }
}
