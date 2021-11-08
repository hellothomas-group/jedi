package xyz.hellothomas.jedi.consumer.common.constants;

/**
 * @author Thomas
 */
public class Constants {
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final String CAFFEINE_CACHE_NAME_EXECUTOR_ALARM = "ExecutorAlarmConfig";
    public static final String CAFFEINE_CACHE_NAME_EXECUTOR_TASK = "ExecutorTask";
    public static final String CAFFEINE_CACHE_NAME_EXECUTOR_INSTANCE = "ExecutorInstance";

    private Constants() {
        throw new IllegalStateException("Constant class");
    }
}
