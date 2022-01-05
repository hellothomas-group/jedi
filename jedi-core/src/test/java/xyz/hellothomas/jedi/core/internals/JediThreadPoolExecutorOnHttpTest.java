package xyz.hellothomas.jedi.core.internals;

import org.junit.Test;
import xyz.hellothomas.jedi.core.dto.consumer.CustomNotification;
import xyz.hellothomas.jedi.core.enums.TaskStatusEnum;
import xyz.hellothomas.jedi.core.internals.executor.JediRunnable;
import xyz.hellothomas.jedi.core.internals.executor.JediThreadPoolExecutor;
import xyz.hellothomas.jedi.core.internals.executor.TaskProperty;
import xyz.hellothomas.jedi.core.internals.message.http.HttpNotificationService;
import xyz.hellothomas.jedi.core.utils.SleepUtil;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class JediThreadPoolExecutorOnHttpTest {

    @Test
    public void shutdown() {
        HttpNotificationService httpNotificationService = new HttpNotificationService("http://127.0.0.1:8080", "LW12" +
                ".04", "dev");
        JediThreadPoolExecutor executor = new JediThreadPoolExecutor(5, 10, 1, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10), "testPool", httpNotificationService);
        executor.submit(() -> {
            System.out.println("execute job...");
            SleepUtil.sleep(1500);
        });
        System.out.println("submit finish");
        executor.shutdown(); // 不在接受新的线程，并且等待之前提交的线程都执行完在关闭
        SleepUtil.sleep(5000);
        System.out.println("main end");
    }

    @Test
    public void shutdownNow() {
        HttpNotificationService httpNotificationService = new HttpNotificationService("http://127.0.0.1:8080", "LW12" +
                ".04", "dev");
        JediThreadPoolExecutor executor = new JediThreadPoolExecutor(5, 10, 1, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10), "testPool", httpNotificationService);
        executor.submit(() -> {
            System.out.println("execute job...");
            SleepUtil.sleep(1500);
        });
        System.out.println("submit finish");
        SleepUtil.sleep(1600); // 先执行完再执行shutdownNow
        executor.shutdownNow(); // 直接关闭活跃状态的所有的线程 ， 并返回等待中的线程
        SleepUtil.sleep(5000);
        System.out.println("main end");
    }

    @Test
    public void afterExecute() {
        HttpNotificationService httpNotificationService = new HttpNotificationService("http://127.0.0.1:8080", "LW12" +
                ".04", "dev");
        JediThreadPoolExecutor executor = new JediThreadPoolExecutor(5, 10, 1, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10), "testPool", httpNotificationService);
        executor.submit(() -> {
            System.out.println("execute job...");
            SleepUtil.sleep(1000);
        });
        System.out.println("submit finish");
        SleepUtil.sleep(15000);
        System.out.println("main end");
    }

    @Test
    public void afterExecuteJediRunnable() {
        HttpNotificationService httpNotificationService = new HttpNotificationService("http://127.0.0.1:8080", "LW12" +
                ".04", "dev");
        JediThreadPoolExecutor executor = new JediThreadPoolExecutor(5, 10, 1, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10), "testPool", httpNotificationService);
        SleepUtil.sleep(2000);
        TaskProperty taskProperty = initTaskProperty(executor.getPoolName(), "taskTest1", null, null);
        executor.submit(new JediRunnable(() -> {
            System.out.println("run start " + LocalDateTime.now());
            System.out.println("execute job...");
            SleepUtil.sleep(1000);
            System.out.println("run finish " + LocalDateTime.now());
        }, taskProperty));
        System.out.println("submit finish");
        SleepUtil.sleep(5000);
        System.out.println("main end");
    }

    @Test
    public void executeJediRunnableRejectException() {
        HttpNotificationService httpNotificationService = new HttpNotificationService("http://127.0.0.1:8080", "LW12" +
                ".04", "dev");
        JediThreadPoolExecutor executor = new JediThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1), "testPool", httpNotificationService);
        int threadCount = 1;
        while (threadCount <= 3) {
            int threadIndex = threadCount++;
            try {
                TaskProperty taskProperty = initTaskProperty(executor.getPoolName(), "taskTest" + threadIndex, null,
                        null);
                executor.execute(new JediRunnable(() -> {
                    System.out.println("execute job" + threadIndex + "...");
                    SleepUtil.sleep(1500);
                }, taskProperty));
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        System.out.println("submit finish");
        SleepUtil.sleep(20000);
        System.out.println("main end");
    }

    @Test
    public void executeRejectException() {
        HttpNotificationService httpNotificationService = new HttpNotificationService("http://127.0.0.1:8080", "LW12" +
                ".04", "dev");
        JediThreadPoolExecutor executor = new JediThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1), "testPool", httpNotificationService);
        int threadCount = 1;
        while (threadCount <= 3) {
            int threadIndex = threadCount++;
            try {
                executor.execute(() -> {
                    System.out.println("execute job" + threadIndex + "...");
                    SleepUtil.sleep(1500);
                });
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        System.out.println("submit finish");
        SleepUtil.sleep(20000);
        System.out.println("main end");
    }

    @Test
    public void execute() {
        HttpNotificationService httpNotificationService = new HttpNotificationService("http://127.0.0.1:8080", "LW12" +
                ".04", "dev");
        JediThreadPoolExecutor executor = new JediThreadPoolExecutor(1, 10, 5, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2), "testPool", httpNotificationService);
        Random random = new Random();
        int threadCount = 1;
        while (threadCount <= 5) {
            int threadIndex = threadCount++;
            try {
                executor.execute(() -> {
                    System.out.println("execute job" + threadIndex + "...");
                    SleepUtil.sleep(random.nextInt(3000));
                });
            } catch (Exception e) {
                System.out.println(e);
            }
            SleepUtil.sleep(50);
        }
        System.out.println("submit finish");
        SleepUtil.sleep(30000);
        System.out.println("main end");
    }

    @Test
    public void sendCustomNotification() {
        HttpNotificationService httpNotificationService = new HttpNotificationService("http://127.0.0.1:8080", "LW12" +
                ".04", "dev");
        CustomNotification customNotification = httpNotificationService.buildCustomNotification("testConent");
        httpNotificationService.pushNotification(customNotification);
        SleepUtil.sleep(30000);
        System.out.println("main end");
    }

    private TaskProperty initTaskProperty(String executorName, String taskName, String taskExtraData,
                                          String dataSource) {
        TaskProperty taskProperty = new TaskProperty();
        taskProperty.setDataSourceName(dataSource);
        taskProperty.setId(UUID.randomUUID().toString());
        taskProperty.setNamespaceName("dev");
        taskProperty.setAppId("test");
        taskProperty.setExecutorName(executorName);
        taskProperty.setTaskName(taskName);
        taskProperty.setTaskExtraData(taskExtraData);
        taskProperty.setCreateTime(LocalDateTime.now());
        taskProperty.setBeanName("");
        taskProperty.setMethodName("");
        taskProperty.setMethodArguments("");
        taskProperty.setStatus(TaskStatusEnum.REGISTERED.getValue());

        return taskProperty;
    }
}
