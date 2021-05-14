package com.hellothomas.jedi.core.internals.executor;

import com.hellothomas.jedi.core.dto.consumer.ExecutorShutdownNotification;
import com.hellothomas.jedi.core.dto.consumer.ExecutorTaskNotification;
import com.hellothomas.jedi.core.dto.consumer.ExecutorTickerNotification;
import com.hellothomas.jedi.core.internals.message.AbstractNotificationService;
import com.hellothomas.jedi.core.utils.SleepUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @className DynamicThreadPoolExecutor
 * @author Thomas
 * @date 2020/12/28 22:54
 * @description
 * @version 1.0
 */
public class DynamicThreadPoolExecutor extends ThreadPoolExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicThreadPoolExecutor.class);

    /**
     * 线程池名称，一般以业务名称命名，方便区分
     */
    private String poolName;

    /**
     * 拒绝计数器
     */
    private AtomicLong rejectCount = new AtomicLong();

    /**
     * 保存任务开始执行的时间，当任务结束时，用任务结束时间减去开始时间计算任务执行时间
     */
    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * 打点线程结束标志
     */
    private volatile boolean toStop = false;

    /**
     * 打点周期，默认5s
     */
    private volatile int tickerCycle = 5000;

    /**
     * 消息服务
     */
    private AbstractNotificationService notificationService;

    public DynamicThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                     BlockingQueue<Runnable> workQueue, String poolName,
                                     AbstractNotificationService notificationService) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.poolName = poolName;
        this.notificationService = notificationService;
        startTickerThread();
    }

    public DynamicThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                     BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
                                     String poolName, AbstractNotificationService notificationService) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        this.poolName = poolName;
        this.notificationService = notificationService;
        startTickerThread();
    }

    public DynamicThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                     BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler,
                                     String poolName, AbstractNotificationService notificationService) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        this.poolName = poolName;
        this.notificationService = notificationService;
        startTickerThread();
    }

    public DynamicThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                     BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
                                     RejectedExecutionHandler handler,
                                     String poolName, AbstractNotificationService notificationService) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        this.poolName = poolName;
        this.notificationService = notificationService;
        startTickerThread();
    }

    public DynamicThreadPoolExecutor(DynamicThreadPoolProperty dynamicThreadPoolProperty) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        super(dynamicThreadPoolProperty.getCorePoolSize(), dynamicThreadPoolProperty.getMaxPoolSize(),
                dynamicThreadPoolProperty.getKeepAliveSeconds(),
                TimeUnit.SECONDS,
                dynamicThreadPoolProperty.getWorkQueue(),
                dynamicThreadPoolProperty.getThreadFactory());
        this.poolName = dynamicThreadPoolProperty.getName();
        this.notificationService = dynamicThreadPoolProperty.getNotificationService();
        this.tickerCycle = dynamicThreadPoolProperty.getTickerCycle();

        if (dynamicThreadPoolProperty.isAllowCoreThreadTimeOut()) {
            this.allowCoreThreadTimeOut(true);
        }

        if (StringUtils.isNotBlank(dynamicThreadPoolProperty.getRejectedExecutionHandler())) {
            this.setRejectedExecutionHandler((RejectedExecutionHandler)
                    Class.forName(dynamicThreadPoolProperty.getRejectedExecutionHandler()).newInstance());
        }

        startTickerThread();
    }

    @Override
    public void shutdown() {
        ExecutorShutdownNotification executorShutdownNotification =
                this.notificationService.buildExecutorShutdownNotification(this.poolName,
                        this.getCompletedTaskCount(),
                        this.getActiveCount(), this.getQueue().size());
        this.notificationService.pushNotification(executorShutdownNotification);
        super.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        // 统计已执行任务、正在执行任务、未执行任务数量
        LOGGER.info("{} Going to immediately shutdown. Executed tasks: {}, Running tasks: {}, Pending tasks: {}",
                this.poolName, this.getCompletedTaskCount(), this.getActiveCount(), this.getQueue().size());
        return super.shutdownNow();
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        this.startTime.set(System.nanoTime());
        super.beforeExecute(t, r);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        long diff = System.nanoTime() - this.startTime.get();

        ExecutorTaskNotification executorTaskNotification =
                this.notificationService.buildExecutorTaskNotification(null, this.poolName,
                        diff);
        this.notificationService.pushNotification(executorTaskNotification);
    }

    @Override
    public void execute(Runnable command) {
        try {
            super.execute(command);
        } catch (RejectedExecutionException e) {
            rejectCount.getAndIncrement();
            throw e;
        }
    }

    @Override
    public Future<?> submit(Runnable task) {
        try {
            return super.submit(task);
        } catch (RejectedExecutionException e) {
            rejectCount.getAndIncrement();
            throw e;
        }
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        try {
            return super.submit(task, result);
        } catch (RejectedExecutionException e) {
            rejectCount.getAndIncrement();
            throw e;
        }
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        try {
            return super.submit(task);
        } catch (RejectedExecutionException e) {
            rejectCount.getAndIncrement();
            throw e;
        }
    }

    /**
     * 启动打点线程
     */
    private void startTickerThread() {
        Thread tickerThread = new Thread(() -> {
            while (!this.toStop) {
                try {
                    ExecutorTickerNotification executorTickerNotification =
                            this.notificationService.buildExecutorTickerNotification(this);

                    this.notificationService.pushNotification(executorTickerNotification);
                } catch (Exception e) {
                    LOGGER.error("打点线程异常: {}", e);
                } finally {
                    SleepUtil.sleep(this.tickerCycle);
                }
            }
        });
        tickerThread.setDaemon(true);
        tickerThread.setName(this.poolName + " tickerThread");
        tickerThread.start();
        LOGGER.debug("{} started!", tickerThread.getName());
    }

    public boolean isToStop() {
        return toStop;
    }

    public void setToStop(boolean toStop) {
        this.toStop = toStop;
    }

    public int getTickerCycle() {
        return tickerCycle;
    }

    public void setTickerCycle(int tickerCycle) {
        this.tickerCycle = tickerCycle;
    }

    public String getPoolName() {
        return poolName;
    }

    public long getRejectCount() {
        return rejectCount.get();
    }

    public AbstractNotificationService getNotificationService() {
        return notificationService;
    }

    public void setNotificationService(AbstractNotificationService notificationService) {
        this.notificationService = notificationService;
    }
}
