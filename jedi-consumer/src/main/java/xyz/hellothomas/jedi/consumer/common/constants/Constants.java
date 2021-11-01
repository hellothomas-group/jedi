package xyz.hellothomas.jedi.consumer.common.constants;

/**
 * @author 80234613
 */
public class Constants {
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final String CAFFEINE_CACHE_NAME_ALARM = "AlarmConfig";
    public static final String CAFFEINE_CACHE_NAME_EXECUTOR_TASK = "ExecutorTask";

    private Constants() {
        throw new IllegalStateException("Utility class");
    }
}
