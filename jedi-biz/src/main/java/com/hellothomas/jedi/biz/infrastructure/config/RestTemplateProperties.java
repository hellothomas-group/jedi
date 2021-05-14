package com.hellothomas.jedi.biz.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @className RestTemplateProperties
 * @author 80234613 唐圆
 * @date 2020-6-6 18:00
 * @descripton
 * @version 1.0
 */
@Data
@ConfigurationProperties(prefix = "rest-template")
public class RestTemplateProperties {
    private static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 30000;
    private static final int DEFAULT_CONNECTION_TIMEOUT = 30000;
    private static final int DEFAULT_READ_TIMEOUT = 30000;

    private int connectionRequestTimeout = DEFAULT_CONNECTION_REQUEST_TIMEOUT;
    private int connectTimeout = DEFAULT_CONNECTION_TIMEOUT;
    private int readTimeout = DEFAULT_READ_TIMEOUT;
}
