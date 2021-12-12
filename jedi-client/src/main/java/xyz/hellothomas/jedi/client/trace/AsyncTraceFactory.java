package xyz.hellothomas.jedi.client.trace;

import java.util.concurrent.Callable;

/**
 * @author Thomas
 * @date 2021/12/12 10:13
 * @description
 * @version 1.0
 */
public interface AsyncTraceFactory<V> {

    Runnable getRunnable(Runnable runnable);

    Callable<V> getCallable(Callable<V> callable);
}
