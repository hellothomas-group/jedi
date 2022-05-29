package xyz.hellothomas.jedi.collector.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.hellothomas.jedi.biz.infrastructure.exception.NotFoundException;
import xyz.hellothomas.jedi.collector.application.AppService;
import xyz.hellothomas.jedi.collector.common.enums.CollectorTypeEnum;
import xyz.hellothomas.jedi.collector.domain.pojo.CollectorProperty;
import xyz.hellothomas.jedi.collector.infrastructure.config.CollectorLocalProperties;
import xyz.hellothomas.jedi.core.dto.ApiResponse;
import xyz.hellothomas.jedi.core.enums.MessageType;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.admin.AdminClientConfig.SECURITY_PROTOCOL_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.*;

/**
 * @author Thomas
 * @date 2021/2/1 22:36
 * @description
 * @version 1.0
 */
@Api(value = "static-config", tags = "static-config")
@RestController
@RequestMapping("/static-config")
@Slf4j
public class StaticConfigController implements EnvironmentAware {
    private final CollectorLocalProperties collectorLocalProperties;
    private final AppService appService;
    private Environment environment;

    public StaticConfigController(CollectorLocalProperties collectorLocalProperties, AppService appService) {
        this.collectorLocalProperties = collectorLocalProperties;
        this.appService = appService;
    }

    @GetMapping(value = "/collector/{namespace}/{appId}")
    @ApiOperation("collector")
    public ApiResponse<CollectorProperty> collector(@PathVariable String namespace, @PathVariable String appId) {
        log.info("namespace:{}, appId:{}", namespace, appId);

        if (appService.findOne(namespace, appId) == null) {
            throw new NotFoundException(String.format("app not found for namespace: %s, appId: %s", namespace, appId));
        }

        Map<String, Object> configProperty = new HashMap<>();
        CollectorProperty collectorProperty = new CollectorProperty();

        if (CollectorTypeEnum.KAFKA.getEnumValue().equals(collectorLocalProperties.getType())) {
            // KAFKA配置
            configProperty.put(BOOTSTRAP_SERVERS_CONFIG, environment.getRequiredProperty("spring.kafka" +
                    ".bootstrap-servers"));
            configProperty.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            configProperty.put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            configProperty.put(SECURITY_PROTOCOL_CONFIG, environment.getProperty("spring.kafka.consumer.properties" +
                    ".security.protocol"));
            configProperty.put("sasl.mechanism", environment.getProperty("spring.kafka.consumer.properties.sasl" +
                    ".mechanism"));
            configProperty.put("sasl.jaas.config", environment.getProperty("spring.kafka.consumer.properties.sasl" +
                    ".jaas.config"));

            // TOPIC配置
            if (collectorLocalProperties.getExecutorTickerTopic() != null) {
                configProperty.put(MessageType.EXECUTOR_TICKER.name(),
                        collectorLocalProperties.getExecutorTickerTopic());
            }
            if (collectorLocalProperties.getExecutorTaskTopic() != null) {
                configProperty.put(MessageType.EXECUTOR_TASK.name(), collectorLocalProperties.getExecutorTaskTopic());
            }
            if (collectorLocalProperties.getExecutorShutdownTopic() != null) {
                configProperty.put(MessageType.EXECUTOR_SHUTDOWN.name(),
                        collectorLocalProperties.getExecutorShutdownTopic());
            }
            if (collectorLocalProperties.getCustomNotificationTopic() != null) {
                configProperty.put(MessageType.CUSTOM_NOTIFICATION.name(),
                        collectorLocalProperties.getCustomNotificationTopic());
            }

            collectorProperty.setType(CollectorTypeEnum.KAFKA.getEnumValue());
            collectorProperty.setConfigDetails(configProperty);
        } else {
            configProperty.put("url", collectorLocalProperties.getUrl());

            collectorProperty.setType(CollectorTypeEnum.HTTP.getEnumValue());
            collectorProperty.setConfigDetails(configProperty);
        }

        return ApiResponse.success(collectorProperty);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
