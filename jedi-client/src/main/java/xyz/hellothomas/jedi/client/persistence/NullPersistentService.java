package xyz.hellothomas.jedi.client.persistence;

import xyz.hellothomas.jedi.client.model.JediTaskExecution;
import xyz.hellothomas.jedi.core.internals.executor.TaskProperty;

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
    public JediTaskExecution queryTaskExecution(TaskProperty taskProperty) {
        throw new NullPointerException("No Available PersistenceServiceImpl");
    }
}
