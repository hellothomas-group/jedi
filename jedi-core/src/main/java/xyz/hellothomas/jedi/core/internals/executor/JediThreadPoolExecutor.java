package xyz.hellothomas.jedi.core.internals.executor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.hellothomas.jedi.core.dto.consumer.ExecutorShutdownNotification;
import xyz.hellothomas.jedi.core.dto.consumer.ExecutorTaskNotification;
import xyz.hellothomas.jedi.core.dto.consumer.ExecutorTickerNotification;
import xyz.hellothomas.jedi.core.enums.TaskStatusEnum;
import xyz.hellothomas.jedi.core.internals.message.AbstractNotificationService;
import xyz.hellothomas.jedi.core.internals.message.NullNotificationService;
import xyz.hellothomas.jedi.core.utils.AsyncContextHolder;
import xyz.hellothomas.jedi.core.utils.SleepUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

import static xyz.hellothomas.jedi.core.constants.Constants.JEDI_DEFAULT_TASK_NAME;

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
    protected void beforeExecute(Thread t, Runnable r) {
        // 任务开始
        TaskProperty taskProperty = initDefaultTaskProperty();
        AsyncAttributes asyncAttributes = new AsyncAttributes();
        asyncAttributes.setAttribute(TaskProperty.class.getName(), taskProperty);
        AsyncContextHolder.setAsyncAttributes(asyncAttributes);
        super.beforeExecute(t, r);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        TaskProperty taskProperty =
                (TaskProperty) AsyncContextHolder.getAsyncAttributes().getAttribute(TaskProperty.class.getName());
        if (taskProperty.getEndTime() == null) {
            // support原生Callable/Runnable
            taskProperty.setEndTime(LocalDateTime.now());
            Throwable throwable = t;
            if (r instanceof RunnableFuture) {
                try {
                    ((RunnableFuture) r).get();
                } catch (Exception e) {
                    LOGGER.error(String.format("taskId:%s, taskName：%s, 执行异常!", taskProperty.getId(),
                            taskProperty.getTaskName()), e);
                    throwable = e;
                }
            }
            if (throwable == null) {
                taskProperty.setStatus(TaskStatusEnum.SUCCESS.getValue());
            } else {
                taskProperty.setStatus(TaskStatusEnum.FAIL.getValue());
                String exceptionString = throwable.getMessage();
                if (exceptionString != null) {
                    exceptionString = exceptionString.length() > 300 ? exceptionString.substring(0,
                            300) : exceptionString;
                    taskProperty.setExitMessage(exceptionString);
                }
            }
        }

        if (!(notificationService instanceof NullNotificationService)) {
            ExecutorTaskNotification executorTaskNotification =
                    this.notificationService.buildExecutorTaskNotification(taskProperty);
            this.notificationService.pushNotification(executorTaskNotification);
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

    private TaskProperty initDefaultTaskProperty() {
        TaskProperty taskProperty = new TaskProperty();
        taskProperty.setId(UUID.randomUUID().toString());
        taskProperty.setExecutorName(this.poolName);
        taskProperty.setTaskName(JEDI_DEFAULT_TASK_NAME);
        LocalDateTime currentTime = LocalDateTime.now();
        taskProperty.setCreateTime(currentTime);
        taskProperty.setStartTime(currentTime);
        taskProperty.setStatus(TaskStatusEnum.DOING.getValue());

        return taskProperty;
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
}
