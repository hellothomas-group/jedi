package com.hellothomas.jedi.core.internals.executor;

import com.hellothomas.jedi.core.internals.message.AbstractNotificationService;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @className DynamicThreadPoolProperty
 * @author Thomas
 * @date 2021/1/23 23:08
 * @description
 * @version 1.0
 */
public class DynamicThreadPoolProperty {
    private static final int DEFAULT_CORE_POOL_SIZE = 10;
    private static final int DEFAULT_MAX_POOL_SIZE = 20;
    private static final int DEFAULT_KEEP_ALIVE_SECONDS = 60;
    private static final int DEFAULT_QUEUE_CAPACITY = 1000;
    private static final int DEFAULT_TICKER_CYCLE = 5000;
    private static final boolean DEFAULT_ALLOW_CORE_THREAD_TIMEOUT = false;
    private static final String DEFAULT_POOL_NAME = "pool-";

    private int corePoolSize = DEFAULT_CORE_POOL_SIZE;
    private int maxPoolSize = DEFAULT_MAX_POOL_SIZE;
    private int keepAliveSeconds = DEFAULT_KEEP_ALIVE_SECONDS;
    private int queueCapacity = DEFAULT_QUEUE_CAPACITY;
    private int tickerCycle = DEFAULT_TICKER_CYCLE;
    private boolean allowCoreThreadTimeOut = DEFAULT_ALLOW_CORE_THREAD_TIMEOUT;
    private BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(queueCapacity);
    private ThreadFactory threadFactory = Executors.defaultThreadFactory();
    private String rejectedExecutionHandler;
    /**
     * not null
     */
    private AbstractNotificationService notificationService;
    private String name = DEFAULT_POOL_NAME + System.currentTimeMillis();

    public static DynamicThreadPoolProperty.DynamicThreadPoolPropertyBuilder builder() {
        return new DynamicThreadPoolProperty.DynamicThreadPoolPropertyBuilder();
    }

    public int getCorePoolSize() {
        return this.corePoolSize;
    }

    public int getMaxPoolSize() {
        return this.maxPoolSize;
    }

    public int getKeepAliveSeconds() {
        return this.keepAliveSeconds;
    }

    public int getQueueCapacity() {
        return this.queueCapacity;
    }

    public boolean isAllowCoreThreadTimeOut() {
        return this.allowCoreThreadTimeOut;
    }

    public String getName() {
        return this.name;
    }

    public String getRejectedExecutionHandler() {
        return this.rejectedExecutionHandler;
    }

    public AbstractNotificationService getNotificationService() {
        return this.notificationService;
    }

    public BlockingQueue<Runnable> getWorkQueue() {
        return this.workQueue;
    }

    public ThreadFactory getThreadFactory() {
        return threadFactory;
    }

    public int getTickerCycle() {
        return tickerCycle;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public void setAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
        this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRejectedExecutionHandler(String rejectedExecutionHandler) {
        this.rejectedExecutionHandler = rejectedExecutionHandler;
    }

    public void setNotificationService(AbstractNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void setWorkQueue(BlockingQueue<Runnable> workQueue) {
        this.workQueue = workQueue;
    }

    public void setThreadFactory(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
    }

    public void setTickerCycle(int tickerCycle) {
        this.tickerCycle = tickerCycle;
    }

    @Override
    public String toString() {
        return "DynamicThreadPoolProperty(corePoolSize=" + this.getCorePoolSize() + ", maxPoolSize=" + this.getMaxPoolSize() + ", keepAliveSeconds=" + this.getKeepAliveSeconds() + ", queueCapacity=" + this.getQueueCapacity() + ", allowCoreThreadTimeOut=" + this.isAllowCoreThreadTimeOut() + ", name=" + this.getName() + ", rejectedExecutionHandler=" + this.getRejectedExecutionHandler() + ", notificationService=" + this.getNotificationService() + ", workQueue=" + this.getWorkQueue() + ", threadFactory=" + this.getThreadFactory() + ", tickerCycle=" + this.getTickerCycle() + ")";
    }

    public DynamicThreadPoolProperty() {
    }

    public DynamicThreadPoolProperty(int corePoolSize, int maxPoolSize, int keepAliveSeconds, int queueCapacity,
                                     boolean allowCoreThreadTimeOut, String name, String rejectedExecutionHandler,
                                     AbstractNotificationService notificationService,
                                     BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, int tickerCycle) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.keepAliveSeconds = keepAliveSeconds;
        this.queueCapacity = queueCapacity;
        this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
        this.name = name;
        this.rejectedExecutionHandler = rejectedExecutionHandler;
        this.notificationService = notificationService;
        this.workQueue = workQueue;
        this.threadFactory = threadFactory;
        this.tickerCycle = tickerCycle;
    }

    public static class DynamicThreadPoolPropertyBuilder {
        private int corePoolSize = DEFAULT_CORE_POOL_SIZE;
        private int maxPoolSize = DEFAULT_MAX_POOL_SIZE;
        private int keepAliveSeconds = DEFAULT_KEEP_ALIVE_SECONDS;
        private int queueCapacity = DEFAULT_QUEUE_CAPACITY;
        private boolean allowCoreThreadTimeOut = DEFAULT_ALLOW_CORE_THREAD_TIMEOUT;
        private String name = DEFAULT_POOL_NAME + System.currentTimeMillis();
        private String rejectedExecutionHandler;
        private AbstractNotificationService notificationService;
        private BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(queueCapacity);
        private ThreadFactory threadFactory = Executors.defaultThreadFactory();
        private int tickerCycle = DEFAULT_TICKER_CYCLE;

        DynamicThreadPoolPropertyBuilder() {
        }

        public DynamicThreadPoolProperty.DynamicThreadPoolPropertyBuilder corePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
            return this;
        }

        public DynamicThreadPoolProperty.DynamicThreadPoolPropertyBuilder maxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
            return this;
        }

        public DynamicThreadPoolProperty.DynamicThreadPoolPropertyBuilder keepAliveSeconds(int keepAliveSeconds) {
            this.keepAliveSeconds = keepAliveSeconds;
            return this;
        }

        public DynamicThreadPoolProperty.DynamicThreadPoolPropertyBuilder queueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
            return this;
        }

        public DynamicThreadPoolProperty.DynamicThreadPoolPropertyBuilder allowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
            this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
            return this;
        }

        public DynamicThreadPoolProperty.DynamicThreadPoolPropertyBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DynamicThreadPoolProperty.DynamicThreadPoolPropertyBuilder rejectedExecutionHandler(String rejectedExecutionHandler) {
            this.rejectedExecutionHandler = rejectedExecutionHandler;
            return this;
        }

        public DynamicThreadPoolProperty.DynamicThreadPoolPropertyBuilder notificationService(AbstractNotificationService notificationService) {
            this.notificationService = notificationService;
            return this;
        }

        public DynamicThreadPoolProperty.DynamicThreadPoolPropertyBuilder workQueue(BlockingQueue<Runnable> workQueue) {
            this.workQueue = workQueue;
            return this;
        }

        public DynamicThreadPoolProperty.DynamicThreadPoolPropertyBuilder threadFactory(ThreadFactory threadFactory) {
            this.threadFactory = threadFactory;
            return this;
        }

        public DynamicThreadPoolProperty.DynamicThreadPoolPropertyBuilder tickerCycle(int tickerCycle) {
            this.tickerCycle = tickerCycle;
            return this;
        }

        public DynamicThreadPoolProperty build() {
            return new DynamicThreadPoolProperty(this.corePoolSize, this.maxPoolSize, this.keepAliveSeconds,
                    this.queueCapacity, this.allowCoreThreadTimeOut, this.name, this.rejectedExecutionHandler,
                    this.notificationService, this.workQueue, this.threadFactory, this.tickerCycle);
        }

        @Override
        public String toString() {
            return "DynamicThreadPoolProperty.DynamicThreadPoolPropertyBuilder(corePoolSize=" + this.corePoolSize +
                    ", maxPoolSize=" + this.maxPoolSize + ", keepAliveSeconds=" + this.keepAliveSeconds + ", " +
                    "queueCapacity=" + this.queueCapacity + ", allowCoreThreadTimeOut=" + this.allowCoreThreadTimeOut + ", name=" + this.name + ", rejectedExecutionHandler=" + this.rejectedExecutionHandler + ", notificationService=" + this.notificationService + ", workQueue=" + this.workQueue + ", threadFactory=" + this.threadFactory + ", tickerCycle=" + this.tickerCycle + ")";
        }
    }
}
