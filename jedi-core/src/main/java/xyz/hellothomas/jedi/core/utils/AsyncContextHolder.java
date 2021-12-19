package xyz.hellothomas.jedi.core.utils;

import xyz.hellothomas.jedi.core.internals.executor.AsyncAttributes;

/**
 * @author Thomas
 * @date 2021/12/16 9:52
 * @descripton
 * @version 1.0
 */
public class AsyncContextHolder {

    private static final ThreadLocal<AsyncAttributes> ASYNC_ATTRIBUTES_HOLDER = new ThreadLocal<>();

    /**
     * Reset the AsyncAttributes for the current thread.
     */
    public static void resetAsyncAttributes() {
        ASYNC_ATTRIBUTES_HOLDER.remove();
    }

    /**
     * Bind the given AsyncAttributes to the current thread.
     * @param asyncAttributes the AsyncAttributes to expose,
     * or {@code null} to reset the thread-bound context
     */
    public static void setAsyncAttributes(AsyncAttributes asyncAttributes) {
        if (asyncAttributes == null) {
            resetAsyncAttributes();
        } else {
            ASYNC_ATTRIBUTES_HOLDER.set(asyncAttributes);
        }
    }

    /**
     * Return the AsyncAttributes currently bound to the thread.
     * @return the AsyncAttributes currently bound to the thread,
     * or {@code null} if none bound
     */
    public static AsyncAttributes getAsyncAttributes() {
        return ASYNC_ATTRIBUTES_HOLDER.get();
    }
}
