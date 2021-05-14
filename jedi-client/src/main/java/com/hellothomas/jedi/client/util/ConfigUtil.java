package com.hellothomas.jedi.client.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.hellothomas.jedi.client.constants.Constants.*;

/**
 * @className SleepUtil
 * @author Thomas
 * @date 2021/3/26 23:45
 * @description
 * @version 1.0
 */
public class ConfigUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigUtil.class);
    private static volatile Boolean enable;
    private static volatile String url;
    private static volatile String namespace;
    private static volatile String appId;

    private ConfigUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isEnable() {
        if (enable != null) {
            return enable;
        }

        enable = Boolean.valueOf(getRequiredProperty(MONITOR_CONFIG_ENABLE_KEY));

        return enable;
    }

    public static String getUrl() {
        if (url != null) {
            return url;
        }

        url = getRequiredProperty(MONITOR_CONFIG_URL_KEY);

        return url;
    }

    public static String getNamespace() {
        if (namespace != null) {
            return namespace;
        }

        namespace = getRequiredProperty(MONITOR_CONFIG_NAMESPACE_KEY);

        return namespace;
    }

    public static String getAppId() {
        if (appId != null) {
            return appId;
        }

        appId = getRequiredProperty(MONITOR_CONFIG_APP_ID_KEY);

        return appId;
    }

    private static String getRequiredProperty(String key) {
        String value = System.getProperty(key);

        if (StringUtils.isBlank(value)) {
            LOGGER.error("key is not set:{}", key);
        }

        return value;
    }
}
