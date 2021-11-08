package xyz.hellothomas.jedi.client.constants;

import java.util.concurrent.TimeUnit;

/**
 * @author Thomas
 * @date 2021/3/23 12:51
 * @descripton
 * @version 1.0
 */
public class Constants {
    public static final int loadConfigQPS = 2; //2 times per second
    public static final int longPollQPS = 2; //2 times per second
    public static final long longPollingInitialDelayInMills = 2000;//2 seconds
    public static final long onErrorRetryInterval = 1;//1 second
    public static final int refreshInterval = 5;
    public static final TimeUnit refreshIntervalTimeUnit = TimeUnit.MINUTES;
    public static final long maxConfigCacheSize = 500;//500 cache key
    public static final long configCacheExpireTime = 1;//1 minute
    public static final TimeUnit configCacheExpireTimeUnit = TimeUnit.MINUTES;//1 minute

    public static final String JEDI_CONFIG_PREFIX = "jedi";
    public static final String JEDI_CONFIG_ENABLE_KEY = "jedi.enable";
    public static final String JEDI_CONFIG_MODE_KEY = "jedi.mode";
    public static final String JEDI_CONFIG_URL_KEY = "jedi.url";
    public static final String JEDI_CONFIG_NAMESPACE_KEY = "jedi.namespace";
    public static final String JEDI_CONFIG_APP_ID_KEY = "jedi.app-id";
    public static final String JEDI_CONFIG_ORDER_KEY = "jedi.order";
    public static final String JEDI_CONFIG_EXECUTORS_KEY = "jedi.executors";
    public static final String JEDI_CONFIG_BEAN_NAME = "jediConfig";

    public static final String JEDI_DEFAULT_EXECUTOR_NAME = "jedi-default-executor";
    public static final int JEDI_ASYNC_DEFAULT_ORDER = 0;
    public static final String JEDI_BOOTSTRAP_PROPERTY_SOURCE_NAME = "JediBootstrapPropertySources";


    private Constants() {
        throw new IllegalStateException("Constant class");
    }
}
