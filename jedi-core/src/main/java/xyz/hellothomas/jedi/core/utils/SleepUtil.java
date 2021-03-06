package xyz.hellothomas.jedi.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas
 * @date 2021/1/2 11:48
 * @description
 * @version 1.0
 */
public class SleepUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(SleepUtil.class);

    private SleepUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            LOGGER.error("休眠异常", e);
        }
    }

    public static void sleepInSecond(long seconds) {
        try {
            Thread.sleep(1000 * seconds);
        } catch (Exception e) {
            LOGGER.error("休眠异常", e);
        }
    }
}
