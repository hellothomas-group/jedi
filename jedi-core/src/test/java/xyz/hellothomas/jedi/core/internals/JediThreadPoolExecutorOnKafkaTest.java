package xyz.hellothomas.jedi.core.internals;

import xyz.hellothomas.jedi.core.dto.consumer.CustomNotification;
import xyz.hellothomas.jedi.core.internals.executor.JediThreadPoolExecutor;
import xyz.hellothomas.jedi.core.internals.executor.JediRunnable;
import xyz.hellothomas.jedi.core.internals.message.kafka.KafkaNotificationService;
import xyz.hellothomas.jedi.core.internals.message.kafka.KafkaProperty;
import xyz.hellothomas.jedi.core.utils.SleepUtil;
import org.apache.kafka.common.security.auth.SecurityProtocol;
import org.apache.kafka.common.security.plain.PlainLoginModule;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.apache.kafka.clients.CommonClientConfigs.SECURITY_PROTOCOL_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.*;

public class JediThreadPoolExecutorOnKafkaTest {

    @Test
    public void shutdown() {
        KafkaNotificationService kafkaNotificationService = new KafkaNotificationService(newKafkaProperty(), "LW12.04"
                , "dev");
        JediThreadPoolExecutor executor = new JediThreadPoolExecutor(5, 10, 1, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10), "testPool", kafkaNotificationService);
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
        KafkaNotificationService kafkaNotificationService = new KafkaNotificationService(newKafkaProperty(), "LW12.04"
                , "dev");
        JediThreadPoolExecutor executor = new JediThreadPoolExecutor(5, 10, 1, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10), "testPool", kafkaNotificationService);
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
        KafkaNotificationService kafkaNotificationService = new KafkaNotificationService(newKafkaProperty(), "LW12.04"
                , "dev");
        JediThreadPoolExecutor executor = new JediThreadPoolExecutor(5, 10, 1, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10), "testPool", kafkaNotificationService);
        executor.submit(() -> {
            System.out.println("execute job...");
            SleepUtil.sleep(1000);
        });
        System.out.println("submit finish");
        SleepUtil.sleep(15000);
        System.out.println("main end");
    }

    @Test
    public void afterExecuteMonitorRunnable() {
        KafkaNotificationService kafkaNotificationService = new KafkaNotificationService(newKafkaProperty(), "LW12.04"
                , "dev");
        JediThreadPoolExecutor executor = new JediThreadPoolExecutor(5, 10, 1, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10), "testPool", kafkaNotificationService);
        SleepUtil.sleep(2000);
        executor.submit(new JediRunnable(executor, "taskTest1",
                () -> {
                    System.out.println("run start " + LocalDateTime.now());
                    System.out.println("execute job...");
                    SleepUtil.sleep(1000);
                    System.out.println("run finish " + LocalDateTime.now());
                }));
        System.out.println("submit finish");
        SleepUtil.sleep(5000);
        System.out.println("main end");
    }

    @Test
    public void executeMonitorRunnableRejectException() {
        KafkaNotificationService kafkaNotificationService = new KafkaNotificationService(newKafkaProperty(), "LW12.04"
                , "dev");
        JediThreadPoolExecutor executor = new JediThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1), "testPool", kafkaNotificationService);
        int threadCount = 1;
        while (threadCount <= 3) {
            int threadIndex = threadCount++;
            try {
                executor.execute(new JediRunnable(executor, "taskTest" + threadIndex,
                        () -> {
                            System.out.println("execute job" + threadIndex + "...");
                            SleepUtil.sleep(1500);
                        }));
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
        KafkaNotificationService kafkaNotificationService = new KafkaNotificationService(newKafkaProperty(), "LW12.04"
                , "dev");
        JediThreadPoolExecutor executor = new JediThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1), "testPool", kafkaNotificationService);
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
        KafkaNotificationService kafkaNotificationService = new KafkaNotificationService(newKafkaProperty(), "LW12.04"
                , "dev");
        JediThreadPoolExecutor executor = new JediThreadPoolExecutor(1, 10, 5, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2), "testPool", kafkaNotificationService);
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
        KafkaNotificationService kafkaNotificationService = new KafkaNotificationService(newKafkaProperty(), "LW12.04"
                , "dev");
        CustomNotification customNotification = kafkaNotificationService.buildCustomNotification("testConent");
        kafkaNotificationService.pushNotification(customNotification);
        SleepUtil.sleep(30000);
        System.out.println("main end");
    }

    private KafkaProperty newKafkaProperty() {
        KafkaProperty loadKafkaProperty = new KafkaProperty();
//        loadKafkaProperty.addProducerConfigValue(BOOTSTRAP_SERVERS_CONFIG, "\n" +
//                "localhost:9092,localhost:9093,localhost:9094");
        loadKafkaProperty.addProducerConfigValue(BOOTSTRAP_SERVERS_CONFIG, "\n" +
                "55.6.73.239:9091,55.6.73.239:9092,55.6.73.239:9093");
        loadKafkaProperty.addProducerConfigValue(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        loadKafkaProperty.addProducerConfigValue(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        String jaasTemplate = PlainLoginModule.class.getName() + " required username=\"%s\" password=" + "\"%s\";";
        String jaasCfg = String.format(jaasTemplate, "LW12.04", "LW12.04");
        loadKafkaProperty.addProducerConfigValue(SECURITY_PROTOCOL_CONFIG, SecurityProtocol.SASL_PLAINTEXT.name);
        loadKafkaProperty.addProducerConfigValue("sasl.mechanism", "PLAIN");
        loadKafkaProperty.addProducerConfigValue("sasl.jaas.config", jaasCfg);

        loadKafkaProperty.setDefaultTopic("custom-message");
        return loadKafkaProperty;
    }
}