package xyz.hellothomas.jedi.core.trace;

import java.util.concurrent.Callable;

/**
 * @author Thomas
 * @date 2021/12/12 10:13
 * @description
 * @version 1.0
 */
public interface AsyncTraceFactory<V> {

    Runnable get(Runnable runnable);

    Callable<V> get(Callable<V> callable);
}
