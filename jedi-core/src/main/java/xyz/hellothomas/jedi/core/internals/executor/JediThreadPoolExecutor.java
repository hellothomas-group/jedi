package xyz.hellothomas.jedi.core.internals.executor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.hellothomas.jedi.core.dto.consumer.ExecutorShutdownNotification;
import xyz.hellothomas.jedi.core.dto.consumer.ExecutorTaskNotification;
import xyz.hellothomas.jedi.core.dto.consumer.ExecutorTickerNotification;
import xyz.hellothomas.jedi.core.internals.message.AbstractNotificationService;
import xyz.hellothomas.jedi.core.internals.message.NullNotificationService;
import xyz.hellothomas.jedi.core.utils.AsyncContextHolder;
import xyz.hellothomas.jedi.core.utils.SleepUtil;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Thomas
 * @date 2020/12/28 22:54
 * @description
 * @version 1.0
 */
public class JediThreadPoolExecutor extends ThreadPoolExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(JediThreadPoolExecutor.class);

    /**
     * 线程池名称，一般以业务名称命名，方便区分
     */
    private String poolName;

    /**
     * 拒绝计数器
     */
    private AtomicLong rejectCount = new AtomicLong();

    /**
     * 打点线程结束标志
     */
    private volatile boolean toStop = false;

    /**
     * 打点周期，默认5s
     */
    private volatile int tickerCycle = 5000;

    /**
     * 上一次拒绝计数器
     */
    private long lastRejectCount = rejectCount.longValue();

    /**
     * 消息服务
     */
    private AbstractNotificationService notificationService;

    public JediThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                  BlockingQueue<Runnable> workQueue, String poolName,
                                  AbstractNotificationService notificationService) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.poolName = poolName;
        this.notificationService = notificationService;
        if (!(notificationService instanceof NullNotificationService)) {
            startTickerThread();
        }
    }

    public JediThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                  BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
                                  String poolName, AbstractNotificationService notificationService) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        this.poolName = poolName;
        this.notificationService = notificationService;
        if (!(notificationService instanceof NullNotificationService)) {
            startTickerThread();
        }
    }

    public JediThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                  BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler,
                                  String poolName, AbstractNotificationService notificationService) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        this.poolName = poolName;
        this.notificationService = notificationService;
        if (!(notificationService instanceof NullNotificationService)) {
            startTickerThread();
        }
    }

    public JediThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                  BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
                                  RejectedExecutionHandler handler,
                                  String poolName, AbstractNotificationService notificationService) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        this.poolName = poolName;
        this.notificationService = notificationService;
        if (!(notificationService instanceof NullNotificationService)) {
            startTickerThread();
        }
    }

    public JediThreadPoolExecutor(JediThreadPoolProperty jediThreadPoolProperty) throws ClassNotFoundException,
            IllegalAccessException, InstantiationException {
        super(jediThreadPoolProperty.getCorePoolSize(), jediThreadPoolProperty.getMaxPoolSize(),
                jediThreadPoolProperty.getKeepAliveSeconds(),
                TimeUnit.SECONDS,
                jediThreadPoolProperty.getWorkQueue(),
                jediThreadPoolProperty.getThreadFactory());
        this.poolName = jediThreadPoolProperty.getName();
        this.notificationService = jediThreadPoolProperty.getNotificationService();
        this.tickerCycle = jediThreadPoolProperty.getTickerCycle();

        if (jediThreadPoolProperty.isAllowCoreThreadTimeOut()) {
            this.allowCoreThreadTimeOut(true);
        }

        if (StringUtils.isNotBlank(jediThreadPoolProperty.getRejectedExecutionHandler())) {
            this.setRejectedExecutionHandler((RejectedExecutionHandler)
                    Class.forName(jediThreadPoolProperty.getRejectedExecutionHandler()).newInstance());
        }

        if (!(notificationService instanceof NullNotificationService)) {
            startTickerThread();
        }
    }

    @Override
    public void shutdown() {
        if (!(notificationService instanceof NullNotificationService)) {
            ExecutorShutdownNotification executorShutdownNotification =
                    this.notificationService.buildExecutorShutdownNotification(this.poolName,
                            this.getCompletedTaskCount(),
                            this.getActiveCount(), this.getQueue().size());
            this.notificationService.pushNotification(executorShutdownNotification);
        }
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
    protected void afterExecute(Runnable r, Throwable t) {
        AsyncAttributes asyncAttributes = AsyncContextHolder.getAsyncAttributes();
        if (asyncAttributes == null) {
            return;
        }
        TaskProperty taskProperty = (TaskProperty) asyncAttributes.getAttribute(TaskProperty.class.getName());
        if (taskProperty == null) {
            return;
        }

        if (!(notificationService instanceof NullNotificationService)) {
            ExecutorTaskNotification executorTaskNotification =
                    this.notificationService.buildExecutorTaskNotification(taskProperty);
            this.notificationService.pushNotification(executorTaskNotification);
        }

        // recover
        if (taskProperty.getCountDownLatch() != null) {
            taskProperty.getCountDownLatch().countDown();
        }

        AsyncContextHolder.resetAsyncAttributes();
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

    public long getLastRejectCount() {
        return lastRejectCount;
    }

    public void setLastRejectCount(long lastRejectCount) {
        this.lastRejectCount = lastRejectCount;
    }

    /**
     * A handler for rejected tasks that runs the rejected task
     * directly in the calling thread of the {@code execute} method,
     * unless the executor has been shut down, in which case the task
     * is discarded.
     */
    public static class JediCallerRunsPolicy implements RejectedExecutionHandler {
        /**
         * Creates a {@code JediCallerRunsPolicy}.
         */
        public JediCallerRunsPolicy() {
        }

        /**
         * Executes task r in the caller's thread, unless the executor
         * has been shut down, in which case the task is discarded.
         *
         * @param r the runnable task requested to be executed
         * @param e the executor attempting to execute this task
         */
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            if (!e.isShutdown()) {
                try {
                    // 任务开始
                    AsyncAttributes asyncAttributes = AsyncContextHolder.getAsyncAttributes();
                    if (asyncAttributes != null) {
                        TaskProperty contextTaskProperty =
                                (TaskProperty) asyncAttributes.getAttribute(TaskProperty.class.getName());
                        // parent
                        if (!contextTaskProperty.isInitialized()) {
                            TaskProperty taskProperty = new TaskProperty();
                            taskProperty.setExecutedByParentTaskThread(true);
                            asyncAttributes.setAttribute(TaskProperty.class.getName(), taskProperty);
                            AsyncContextHolder.setAsyncAttributes(asyncAttributes);
                        }
                    }

                    r.run();
                } finally {
                    AsyncAttributes asyncAttributes = AsyncContextHolder.getAsyncAttributes();
                    if (asyncAttributes == null) {
                        return;
                    }
                    TaskProperty taskProperty =
                            (TaskProperty) asyncAttributes.getAttribute(TaskProperty.class.getName());
                    if (taskProperty == null) {
                        return;
                    }

                    AbstractNotificationService notificationService =
                            ((JediThreadPoolExecutor) e).getNotificationService();
                    if (!(notificationService instanceof NullNotificationService)) {
                        ExecutorTaskNotification executorTaskNotification =
                                notificationService.buildExecutorTaskNotification(taskProperty);
                        notificationService.pushNotification(executorTaskNotification);
                    }

                    // recover
                    if (taskProperty.getCountDownLatch() != null) {
                        taskProperty.getCountDownLatch().countDown();
                    }

                    if (taskProperty.getParentTaskProperty() == null) {
                        AsyncContextHolder.resetAsyncAttributes();
                    } else {
                        resumeAsyncAttributes(taskProperty.getParentTaskProperty());
                    }
                }
            }
        }

        private AsyncAttributes resumeAsyncAttributes(TaskProperty parentTaskProperty) {
            AsyncAttributes asyncAttributes = AsyncContextHolder.getAsyncAttributes();
            asyncAttributes.setAttribute(TaskProperty.class.getName(), parentTaskProperty);
            return asyncAttributes;
        }
    }
}
