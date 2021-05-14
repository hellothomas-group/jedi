package com.hellothomas.jedi.consumer.api;

import com.hellothomas.jedi.consumer.common.enums.ConsumerTypeEnum;
import com.hellothomas.jedi.consumer.infrastructure.config.ConsumerProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.security.auth.SecurityProtocol;
import org.apache.kafka.common.security.plain.PlainLoginModule;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.admin.AdminClientConfig.SECURITY_PROTOCOL_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.*;

/**
 * @className StaticConfigController
 * @author Thomas
 * @date 2021/2/1 22:36
 * @description
 * @version 1.0
 */
@Api(value = "static-config", tags = "static-config")
@RestController
@RequestMapping("/static-config")
@Slf4j
public class StaticConfigController {
    @Value("${consumer.type}")
    private String consumerType;

    @GetMapping(value = "/consumer/{namespace}/{appId}")
    @ApiOperation("consumer")
    public ConsumerProperty consumer(@PathVariable String namespace, @PathVariable String appId) {
        log.info("namespace:{}, appId:{}", namespace, appId);
        Map<String, Object> configProperty = new HashMap<>();
        ConsumerProperty consumerProperty = new ConsumerProperty();

        if (ConsumerTypeEnum.KAFKA.getEnumValue().equals(consumerType)) {
            configProperty.put(BOOTSTRAP_SERVERS_CONFIG, "55.6.73.239:9091,55.6.73.239:9092,55.6.73.239:9093");
            configProperty.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            configProperty.put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

            String jaasTemplate = PlainLoginModule.class.getName() + " required username=\"%s\" password=" + "\"%s\";";
            String jaasCfg = String.format(jaasTemplate, "LW12.04", "LW12.04");
            configProperty.put(SECURITY_PROTOCOL_CONFIG, SecurityProtocol.SASL_PLAINTEXT.name);
            configProperty.put("sasl.mechanism", "PLAIN");
            configProperty.put("sasl.jaas.config", jaasCfg);

            consumerProperty.setType(ConsumerTypeEnum.KAFKA.getEnumValue());
            consumerProperty.setConfigDetails(configProperty);
        } else {
            configProperty.put("url", "http://127.0.0.1:8080");

            consumerProperty.setType(ConsumerTypeEnum.HTTP.getEnumValue());
            consumerProperty.setConfigDetails(configProperty);
        }

        return consumerProperty;
    }
}
