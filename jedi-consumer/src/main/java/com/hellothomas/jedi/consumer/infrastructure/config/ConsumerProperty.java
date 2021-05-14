package com.hellothomas.jedi.consumer.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * @className ConsumerProperty
 * @author Thomas
 * @date 2021/4/14 22:55
 * @description
 * @version 1.0
 */
@Getter
@Setter
@ToString
public class ConsumerProperty {
    private String type;

    private Map<String, Object> configDetails;
}
