package xyz.hellothomas.jedi.client.config;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.reflect.TypeToken;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import xyz.hellothomas.jedi.client.constants.Constants;
import xyz.hellothomas.jedi.client.enums.ConsumerTypeEnum;
import xyz.hellothomas.jedi.client.util.ExceptionUtil;
import xyz.hellothomas.jedi.client.util.HttpRequest;
import xyz.hellothomas.jedi.client.util.HttpResponse;
import xyz.hellothomas.jedi.client.util.HttpUtil;
import xyz.hellothomas.jedi.core.internals.executor.DynamicThreadPoolExecutor;
import xyz.hellothomas.jedi.core.internals.executor.DynamicThreadPoolProperty;
import xyz.hellothomas.jedi.core.internals.message.AbstractNotificationService;
import xyz.hellothomas.jedi.core.internals.message.NullNotificationService;
import xyz.hellothomas.jedi.core.internals.message.http.HttpNotificationService;
import xyz.hellothomas.jedi.core.internals.message.kafka.KafkaNotificationService;
import xyz.hellothomas.jedi.core.internals.message.kafka.KafkaProperty;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 80234613 唐圆
 * @date 2021/4/12 10:17
 * @descripton
 * @version 1.0
 */
@Slf4j
public class ExecutorRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware, BeanFactoryAware {
    private static final String NOTIFICATION_SERVICE_BEAN_NAME = "notificationService";
    private static final String MONITOR_CONFIG_BEAN_NAME = "monitorConfig";
    private static final String CONSUMER_HTTP_KEY = "url";
    private static final Splitter EXECUTOR_SPLITTER = Splitter.on(",").omitEmptyStrings().trimResults();
    private static final Joiner STRING_JOINER_PROPERTY = Joiner.on(".");

    private MonitorConfig monitorConfig;
    private BeanFactory beanFactory;

    @SneakyThrows
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        ((DefaultListableBeanFactory) this.beanFactory).registerSingleton(MONITOR_CONFIG_BEAN_NAME,
                this.monitorConfig);
        log.info("{} 注册beanFactory", MONITOR_CONFIG_BEAN_NAME);

        AbstractNotificationService notificationService = buildNotificationService(monitorConfig.getUrl(),
                monitorConfig.getNamespace(), monitorConfig.getAppId());

        ((DefaultListableBeanFactory) this.beanFactory).registerSingleton(NOTIFICATION_SERVICE_BEAN_NAME,
                notificationService);
        log.info("{} 注册beanFactory", NOTIFICATION_SERVICE_BEAN_NAME);

        monitorConfig.getExecutors().stream()
                .filter(i -> i != null && !Constants.DEFAULT_EXECUTOR_NAME.equals(i.getName()))
                .forEach(j -> {
                    j.setNotificationService(notificationService);

                    GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
                    genericBeanDefinition.setBeanClass(DynamicThreadPoolExecutor.class);

                    ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
                    constructorArgumentValues.addIndexedArgumentValue(0, j);
                    genericBeanDefinition.setConstructorArgumentValues(constructorArgumentValues);

                    registry.registerBeanDefinition(j.getName(), genericBeanDefinition);
                    log.info("{} 注册beanFactory", j.getName());
                });
    }

    @Override
    public void setEnvironment(Environment environment) {
        Iterable sources = ConfigurationPropertySources.get(environment);
        Binder binder = new Binder(sources);
        BindResult<MonitorProperty> bindResult = binder.bind(Constants.MONITOR_CONFIG_PREFIX, MonitorProperty.class);

        MonitorProperty monitorProperty = bindResult.orElse(null);

        monitorConfig = new MonitorConfig();
        BeanUtils.copyProperties(monitorProperty, monitorConfig);
        monitorConfig.setExecutors(buildMonitorConfigExecutors(monitorProperty.getExecutors(), binder));

        log.debug("monitorConfig:{}", monitorConfig);
    }

    private AbstractNotificationService buildNotificationService(String url, String namespace, String appId) {
        try {
            // 从远端获取consumerUrl或者kafkaProperty
            String getConsumerEndpointUrl = String.format("%s/static-config/consumer/%s/%s", url, namespace, appId);

            HttpRequest request = new HttpRequest(getConsumerEndpointUrl);
            log.debug("request:{}", request);

            Type responseType = new TypeToken<ConsumerProperty>() {
            }.getType();
            HttpResponse<ConsumerProperty> response = HttpUtil.doGet(request, responseType);

            // 实例化notificationService
            if (ConsumerTypeEnum.HTTP.getEnumValue().equals(response.getBody().getType())) {
                String notificationUrl = (String) response.getBody().getConfigDetails().get(CONSUMER_HTTP_KEY);
                return new HttpNotificationService(notificationUrl, appId, namespace);
            } else if (ConsumerTypeEnum.KAFKA.getEnumValue().equals(response.getBody().getType())) {
                KafkaProperty kafkaProperty = new KafkaProperty();
                kafkaProperty.setProducerConfig(response.getBody().getConfigDetails());
                return new KafkaNotificationService(kafkaProperty, appId, namespace);
            }
        } catch (Exception e) {
            log.error("buildNotificationService error:{}", ExceptionUtil.getDetailMessage(e));
        }

        return new NullNotificationService(appId, namespace);
    }

    private List<DynamicThreadPoolProperty> buildMonitorConfigExecutors(String executors, Binder binder) {
        List<String> executorNames = EXECUTOR_SPLITTER.splitToList(executors);
        List<DynamicThreadPoolProperty> executorProperties = new ArrayList<>();
        for (String executorName : executorNames) {
            DynamicThreadPoolProperty dynamicThreadPoolProperty = buildDynamicThreadPoolProperty(executorName, binder);
            executorProperties.add(dynamicThreadPoolProperty);
        }

        return executorProperties;
    }

    private DynamicThreadPoolProperty buildDynamicThreadPoolProperty(String executor, Binder binder) {
        String dynamicThreadPoolPropertyPrefix = STRING_JOINER_PROPERTY.join(Constants.MONITOR_CONFIG_PREFIX, executor);
        BindResult<DynamicThreadPoolProperty> bindResult = binder.bind(dynamicThreadPoolPropertyPrefix,
                DynamicThreadPoolProperty.class);
        DynamicThreadPoolProperty dynamicThreadPoolProperty =
                bindResult.orElse(DynamicThreadPoolProperty.builder().build());
        dynamicThreadPoolProperty.setName(executor);

        return dynamicThreadPoolProperty;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
