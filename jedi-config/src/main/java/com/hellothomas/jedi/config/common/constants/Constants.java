package com.hellothomas.jedi.config.common.constants;

/**
 * @className Constants
 * @author 80234613 唐圆
 * @date 2021/3/23 12:51
 * @descripton
 * @version 1.0
 */
public class Constants {
    public static final long DEFAULT_LONG_POLLING_TIMEOUT = 60000;
    public static final int DEFAULT_RELEASE_MESSAGE_NOTIFICATION_BATCH = 100;
    public static final int DEFAULT_RELEASE_MESSAGE_NOTIFICATION_BATCH_INTERVAL_IN_MILLI = 100;
    public static final int DEFAULT_RELEASE_MESSAGE_SCAN_INTERVAL_IN_MS = 1000;

    private Constants() {
        throw new IllegalStateException("Utility class");
    }
}
