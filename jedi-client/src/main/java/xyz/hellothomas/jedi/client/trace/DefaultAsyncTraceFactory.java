package xyz.hellothomas.jedi.client.trace;

import java.util.concurrent.Callable;

/**
 * @author Thomas
 * @date 2021/12/12 10:15
 * @description
 * @version 1.0
 */
public class DefaultAsyncTraceFactory<V> implements AsyncTraceFactory<V> {
    @Override
    public Runnable getRunnable(Runnable runnable) {
        return runnable;
    }

    @Override
    public Callable<V> getCallable(Callable<V> callable) {
        return callable;
    }
}
