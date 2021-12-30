package xyz.hellothomas.jedi.client.internals;

import org.springframework.lang.Nullable;

/**
 * @author Thomas
 * @date 2021/12/26 22:44
 * @description
 * @version 1.0
 */
public interface RetryTaskService {

    void retry(String taskId, @Nullable String dataSourceName, String operator);
}
