package xyz.hellothomas.jedi.client.internals;

import org.springframework.lang.Nullable;
import xyz.hellothomas.jedi.core.internals.executor.TaskProperty;

/**
 * @author Thomas
 * @date 2021/12/26 22:44
 * @description
 * @version 1.0
 */
public interface RetryTaskService {

    TaskProperty retry(String taskId, @Nullable String dataSourceName, String operator);
}
