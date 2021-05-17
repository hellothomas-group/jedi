package xyz.hellothomas.jedi.client.internals;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;
import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.hellothomas.jedi.client.constants.Constants;
import xyz.hellothomas.jedi.client.enums.ConfigSourceType;
import xyz.hellothomas.jedi.client.exception.JediClientException;
import xyz.hellothomas.jedi.client.exception.JediClientStatusCodeException;
import xyz.hellothomas.jedi.client.schedule.ExponentialSchedulePolicy;
import xyz.hellothomas.jedi.client.schedule.SchedulePolicy;
import xyz.hellothomas.jedi.client.util.ConfigUtil;
import xyz.hellothomas.jedi.client.util.HttpRequest;
import xyz.hellothomas.jedi.client.util.HttpResponse;
import xyz.hellothomas.jedi.client.util.HttpUtil;
import xyz.hellothomas.jedi.core.dto.config.JediExecutorConfig;
import xyz.hellothomas.jedi.core.utils.JediThreadFactory;
import xyz.hellothomas.jedi.core.utils.NetUtil;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static xyz.hellothomas.jedi.core.constants.Constants.NAMESPACE_EXECUTOR_SEPARATOR;

public class RemoteConfigRepository extends AbstractConfigRepository {
    private static final Logger logger = LoggerFactory.getLogger(RemoteConfigRepository.class);
    private static final Joiner STRING_JOINER = Joiner.on(NAMESPACE_EXECUTOR_SEPARATOR);
    private static final Joiner STRING_JOINER_PROPERTY = Joiner.on(".");
    private static final Joiner.MapJoiner MAP_JOINER = Joiner.on("&").withKeyValueSeparator("=");
    private static final Escaper pathEscaper = UrlEscapers.urlPathSegmentEscaper();
    private static final Escaper queryParamEscaper = UrlEscapers.urlFormParameterEscaper();

    private final RemoteConfigLongPollService m_remoteConfigLongPollService;
    private volatile AtomicReference<JediExecutorConfig> m_configCache;
    private final String url;
    private final String namespace;
    private final String appId;
    private final String executor;
    private final static ScheduledExecutorService m_executorService;
    private final AtomicLong m_remoteNotificationId;
    private final RateLimiter m_loadConfigRateLimiter;
    private final SchedulePolicy m_loadConfigFailSchedulePolicy;
    private final Gson gson;

    static {
        m_executorService = Executors.newScheduledThreadPool(1,
                JediThreadFactory.create("RemoteConfigRepository", true));
    }

    /**
     * Constructor
     *
     * @param executor
     * @param remoteConfigLongPollService
     */
    public RemoteConfigRepository(String executor, RemoteConfigLongPollService remoteConfigLongPollService) {
        this.url = ConfigUtil.getUrl();
        this.namespace = ConfigUtil.getNamespace();
        this.appId = ConfigUtil.getAppId();
        this.executor = executor;
        m_configCache = new AtomicReference<>();
        m_remoteConfigLongPollService = remoteConfigLongPollService;
        m_remoteNotificationId = new AtomicLong();
        m_loadConfigRateLimiter = RateLimiter.create(Constants.loadConfigQPS);
        m_loadConfigFailSchedulePolicy = new ExponentialSchedulePolicy(Constants.onErrorRetryInterval,
                Constants.onErrorRetryInterval * 8);
        gson = new Gson();
        this.trySync();
        this.schedulePeriodicRefresh();
        this.scheduleLongPollingRefresh();
    }

    @Override
    public Properties getConfig() {
        if (m_configCache.get() == null) {
            this.sync();
        }
        return transformJediConfigToProperties(m_configCache.get());
    }

    @Override
    public ConfigSourceType getSourceType() {
        return ConfigSourceType.REMOTE;
    }

    private void schedulePeriodicRefresh() {
        logger.debug("Schedule periodic refresh with interval: {} {}",
                Constants.refreshInterval, Constants.refreshIntervalTimeUnit);
        m_executorService.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        logger.debug("refresh config for executor: {}", executor);
                        trySync();
                    }
                }, Constants.refreshInterval, Constants.refreshInterval, Constants.refreshIntervalTimeUnit);
    }

    @Override
    protected synchronized void sync() {
        JediExecutorConfig previous = m_configCache.get();
        JediExecutorConfig current = loadJediConfig();

        //reference equals means HTTP 304
        if (previous != current) {
            logger.debug("Remote Config refreshed! {}", executor);
            m_configCache.set(current);
            this.fireRepositoryChange(executor, this.getConfig());
        }

        if (current != null) {
            logger.debug(String.format("Jedi.Client.Configs.%s", current.getExecutorName()),
                    current.getReleaseKey());
        }
    }

    private Properties transformJediConfigToProperties(JediExecutorConfig jediExecutorConfig) {
        Properties result = new Properties();
        jediExecutorConfig.getConfigurations().forEach((key, value) -> {
            String propertyKey = STRING_JOINER_PROPERTY.join(Constants.MONITOR_CONFIG_PREFIX,
                    jediExecutorConfig.getExecutorName(), key);
            result.put(propertyKey, value);
        });
        return result;
    }

    private JediExecutorConfig loadJediConfig() {
        if (!m_loadConfigRateLimiter.tryAcquire(5, TimeUnit.SECONDS)) {
            //wait at most 5 seconds
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
            }
        }

        logger.trace("Jedi.Client.ConfigMeta {}", STRING_JOINER.join(namespace, appId, executor));
        Throwable exception = null;

        String queryUrl = assembleQueryConfigUrl(url, namespace, appId, executor, m_remoteNotificationId.get(),
                m_configCache.get());

        logger.debug("Loading config from {}", queryUrl);

        HttpRequest request = new HttpRequest(queryUrl);

        try {

            HttpResponse<JediExecutorConfig> response = HttpUtil.doGet(request, JediExecutorConfig.class);
            m_loadConfigFailSchedulePolicy.success();

            if (response.getStatusCode() == 304) {
                logger.debug("Config server responds with 304 HTTP status code.");
                return m_configCache.get();
            }

            JediExecutorConfig result = response.getBody();

            logger.debug("Loaded config for {}: {}", executor, result);

            return result;
        } catch (JediClientStatusCodeException ex) {
            JediClientStatusCodeException statusCodeException = ex;
            //config not found
            if (ex.getStatusCode() == 404) {
                String message = String.format(
                        "Could not find config for executor - namespace: %s, appId: %s " +
                                "please check whether the configs are released in Jedi!",
                        namespace, appId);
                statusCodeException = new JediClientStatusCodeException(ex.getStatusCode(),
                        message);
            }
            exception = statusCodeException;
        } catch (Throwable ex) {
            exception = ex;
        }

        String message = String.format(
                "Load Jedi Config failed - namespace: %s, appId: %s, executor: %s, queryUrl: %s",
                namespace, appId, executor, queryUrl);
        throw new JediClientException(message, exception);
    }

    String assembleQueryConfigUrl(String uri, String namespace, String appId, String executor,
                                  long remoteNotificationId,
                                  JediExecutorConfig previousConfig) {

        String path = "configs/%s/%s/%s";
        List<String> pathParams =
                Lists.newArrayList(pathEscaper.escape(namespace), pathEscaper.escape(appId),
                        pathEscaper.escape(executor));
        Map<String, String> queryParams = Maps.newHashMap();

        if (previousConfig != null) {
            queryParams.put("releaseKey", queryParamEscaper.escape(previousConfig.getReleaseKey()));
        }

        String localIp = NetUtil.getLocalHost();
        if (!Strings.isNullOrEmpty(localIp)) {
            queryParams.put("ip", queryParamEscaper.escape(localIp));
        }

        if (remoteNotificationId > 0) {
            queryParams.put("notificationId", String.valueOf(remoteNotificationId));
        }

        String pathExpanded = String.format(path, pathParams.toArray());

        if (!queryParams.isEmpty()) {
            pathExpanded += "?" + MAP_JOINER.join(queryParams);
        }
        if (!uri.endsWith("/")) {
            uri += "/";
        }
        return uri + pathExpanded;
    }

    private void scheduleLongPollingRefresh() {
        m_remoteConfigLongPollService.submit(executor, this);
    }

    public void onLongPollNotified(long remoteNotificationId) {
        m_remoteNotificationId.set(remoteNotificationId);
        m_executorService.submit(new Runnable() {
            @Override
            public void run() {
                trySync();
            }
        });
    }

}
