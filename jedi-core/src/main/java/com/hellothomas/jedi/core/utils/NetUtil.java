package com.hellothomas.jedi.core.utils;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

/**
 * @className NetUtil
 * @author Thomas
 * @date 2021/1/17 16:40
 * @description
 * @version 1.0
 */
public class NetUtil {
    private static final String DEFAULT_HOST_ADDRESS = "UNKNOWN";
    private static final Logger LOGGER = LoggerFactory.getLogger(NetUtil.class);

    private NetUtil() {
        throw new IllegalStateException("Utility Class");
    }

    public static String getLocalHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            LOGGER.error("getLocalHostError: {}", ExceptionUtils.getStackTrace(e));
            return DEFAULT_HOST_ADDRESS;
        }
    }
}
