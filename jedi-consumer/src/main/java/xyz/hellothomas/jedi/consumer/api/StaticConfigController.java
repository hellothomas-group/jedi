package xyz.hellothomas.jedi.consumer.api;

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
import xyz.hellothomas.jedi.consumer.application.AppService;
import xyz.hellothomas.jedi.consumer.common.enums.ConsumerTypeEnum;
import xyz.hellothomas.jedi.consumer.domain.pojo.ConsumerProperty;
import xyz.hellothomas.jedi.consumer.infrastructure.config.ConsumerLocalProperties;
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
    private final ConsumerLocalProperties consumerLocalProperties;
    private final AppService appService;
    private Environment environment;

    public StaticConfigController(ConsumerLocalProperties consumerLocalProperties, AppService appService) {
        this.consumerLocalProperties = consumerLocalProperties;
        this.appService = appService;
    }

    @GetMapping(value = "/consumer/{namespace}/{appId}")
    @ApiOperation("consumer")
    public ApiResponse<ConsumerProperty> consumer(@PathVariable String namespace, @PathVariable String appId) {
        log.info("namespace:{}, appId:{}", namespace, appId);

        if (appService.findOne(namespace, appId) == null) {
            throw new NotFoundException(String.format("app not found for namespace: %s, appId: %s", namespace, appId));
        }

        Map<String, Object> configProperty = new HashMap<>();
        ConsumerProperty consumerProperty = new ConsumerProperty();

        if (ConsumerTypeEnum.KAFKA.getEnumValue().equals(consumerLocalProperties.getType())) {
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
            if (consumerLocalProperties.getExecutorTickerTopic() != null) {
                configProperty.put(MessageType.EXECUTOR_TICKER.name(),
                        consumerLocalProperties.getExecutorTickerTopic());
            }
            if (consumerLocalProperties.getExecutorTaskTopic() != null) {
                configProperty.put(MessageType.EXECUTOR_TASK.name(), consumerLocalProperties.getExecutorTaskTopic());
            }
            if (consumerLocalProperties.getExecutorShutdownTopic() != null) {
                configProperty.put(MessageType.EXECUTOR_SHUTDOWN.name(),
                        consumerLocalProperties.getExecutorShutdownTopic());
            }
            if (consumerLocalProperties.getCustomNotificationTopic() != null) {
                configProperty.put(MessageType.CUSTOM_NOTIFICATION.name(),
                        consumerLocalProperties.getCustomNotificationTopic());
            }

            consumerProperty.setType(ConsumerTypeEnum.KAFKA.getEnumValue());
            consumerProperty.setConfigDetails(configProperty);
        } else {
            configProperty.put("url", consumerLocalProperties.getUrl());

            consumerProperty.setType(ConsumerTypeEnum.HTTP.getEnumValue());
            consumerProperty.setConfigDetails(configProperty);
        }

        return ApiResponse.success(consumerProperty);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
