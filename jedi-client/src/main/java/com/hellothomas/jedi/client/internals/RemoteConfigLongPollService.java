package com.hellothomas.jedi.client.internals;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;
import com.hellothomas.jedi.client.constants.Constants;
import com.hellothomas.jedi.client.exception.JediConfigException;
import com.hellothomas.jedi.client.schedule.ExponentialSchedulePolicy;
import com.hellothomas.jedi.client.schedule.SchedulePolicy;
import com.hellothomas.jedi.client.util.*;
import com.hellothomas.jedi.core.dto.config.JediConfigNotification;
import com.hellothomas.jedi.core.utils.JediThreadFactory;
import com.hellothomas.jedi.core.utils.NetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.hellothomas.jedi.core.constants.Constants.NAMESPACE_EXECUTOR_SEPARATOR;
import static com.hellothomas.jedi.core.constants.Constants.NOTIFICATION_ID_PLACEHOLDER;

public class RemoteConfigLongPollService {
    private static final Logger logger = LoggerFactory.getLogger(RemoteConfigLongPollService.class);
    private static final Joiner STRING_JOINER = Joiner.on(NAMESPACE_EXECUTOR_SEPARATOR);
    private static final Joiner.MapJoiner MAP_JOINER = Joiner.on("&").withKeyValueSeparator("=");
    private static final Escaper queryParamEscaper = UrlEscapers.urlFormParameterEscaper();
    private static final long INIT_NOTIFICATION_ID = NOTIFICATION_ID_PLACEHOLDER;
    //90 seconds, should be longer than server side's long polling timeout, which is now 60 seconds
    private static final int LONG_POLLING_READ_TIMEOUT = 90 * 1000;
    private final ExecutorService m_longPollingService;
    private final AtomicBoolean m_longPollingStopped;
    private SchedulePolicy m_longPollFailSchedulePolicyInSecond;
    private RateLimiter m_longPollRateLimiter;
    private final AtomicBoolean m_longPollStarted;
    private final ConcurrentMap<String, RemoteConfigRepository> m_longPollExecutors;
    private final ConcurrentMap<String, Long> m_notifications;
    private final String url;
    private final String namespace;
    private final String appId;
    private Type m_responseType;
    private Gson gson;

    /**
     * Constructor.
     */
    public RemoteConfigLongPollService() {
        m_longPollFailSchedulePolicyInSecond = new ExponentialSchedulePolicy(1, 120); //in second
        m_longPollingStopped = new AtomicBoolean(false);
        m_longPollingService = Executors.newSingleThreadExecutor(
                JediThreadFactory.create("RemoteConfigLongPollService", true));
        m_longPollStarted = new AtomicBoolean(false);
        m_longPollExecutors = Maps.newConcurrentMap();
        m_notifications = Maps.newConcurrentMap();
        this.url = ConfigUtil.getUrl();
        this.namespace = ConfigUtil.getNamespace();
        this.appId = ConfigUtil.getAppId();

        m_responseType = new TypeToken<List<JediConfigNotification>>() {
        }.getType();
        gson = new Gson();
        m_longPollRateLimiter = RateLimiter.create(Constants.longPollQPS);
    }

    public void submit(String executor, RemoteConfigRepository remoteConfigRepository) {
        m_longPollExecutors.put(executor, remoteConfigRepository);
        m_notifications.putIfAbsent(executor, INIT_NOTIFICATION_ID);
        if (!m_longPollStarted.get()) {
            startLongPolling();
        }
    }

    private void startLongPolling() {
        if (!m_longPollStarted.compareAndSet(false, true)) {
            //already started
            return;
        }
        try {
            final long longPollingInitialDelayInMills = Constants.longPollingInitialDelayInMills;
            m_longPollingService.submit(new Runnable() {
                @Override
                public void run() {
                    if (longPollingInitialDelayInMills > 0) {
                        try {
                            logger.debug("Long polling will start in {} ms.", longPollingInitialDelayInMills);
                            TimeUnit.MILLISECONDS.sleep(longPollingInitialDelayInMills);
                        } catch (InterruptedException e) {
                            //ignore
                        }
                    }
                    doLongPollingRefresh(namespace, appId);
                }
            });
        } catch (Throwable ex) {
            m_longPollStarted.set(false);
            JediConfigException exception =
                    new JediConfigException("Schedule long polling refresh failed", ex);
            logger.warn(ExceptionUtil.getDetailMessage(exception));
        }
    }

    void stopLongPollingRefresh() {
        this.m_longPollingStopped.compareAndSet(false, true);
    }

    private void doLongPollingRefresh(String namespace, String appId) {
        while (!m_longPollingStopped.get() && !Thread.currentThread().isInterrupted()) {
            if (!m_longPollRateLimiter.tryAcquire(5, TimeUnit.SECONDS)) {
                //wait at most 5 seconds
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                }
            }
            String queryUrl = null;
            try {
                queryUrl =
                        assembleLongPollRefreshUrl(url, namespace, appId, m_notifications);

                logger.debug("Long polling from {}", queryUrl);

                HttpRequest request = new HttpRequest(queryUrl);
                request.setReadTimeout(LONG_POLLING_READ_TIMEOUT);

                final HttpResponse<List<JediConfigNotification>> response =
                        HttpUtil.doGet(request, m_responseType);

                logger.debug("Long polling response: {}, queryUrl: {}", response.getStatusCode(), queryUrl);
                if (response.getStatusCode() == 200 && response.getBody() != null) {
                    updateNotifications(response.getBody());
                    notify(response.getBody());
                }

                m_longPollFailSchedulePolicyInSecond.success();
            } catch (Throwable ex) {
                long sleepTimeInSecond = m_longPollFailSchedulePolicyInSecond.fail();
                logger.warn(
                        "Long polling failed, will retry in {} seconds. namespace: {}, appId: {}, executors: {}, " +
                                "long polling queryUrl: {}, reason: {}",
                        sleepTimeInSecond, namespace, appId, assembleExecutors(), queryUrl,
                        ExceptionUtil.getDetailMessage(ex));
                try {
                    TimeUnit.SECONDS.sleep(sleepTimeInSecond);
                } catch (InterruptedException ie) {
                    //ignore
                }
            }
        }
    }

    private void notify(List<JediConfigNotification> notifications) {
        if (notifications == null || notifications.isEmpty()) {
            return;
        }
        for (JediConfigNotification notification : notifications) {
            String executorName = notification.getExecutorName();
            //create a new list to avoid ConcurrentModificationException
            RemoteConfigRepository toBeNotified = m_longPollExecutors.get(executorName);
            try {
                toBeNotified.onLongPollNotified(notification.getNotificationId());
            } catch (Throwable ex) {
                logger.error("onLongPollNotified error:{}", ex);
            }
        }
    }

    private void updateNotifications(List<JediConfigNotification> deltaNotifications) {
        for (JediConfigNotification notification : deltaNotifications) {
            if (Strings.isNullOrEmpty(notification.getExecutorName())) {
                continue;
            }
            String executorName = notification.getExecutorName();
            if (m_notifications.containsKey(executorName)) {
                m_notifications.put(executorName, notification.getNotificationId());
            }
        }
    }

    private String assembleExecutors() {
        return STRING_JOINER.join(m_longPollExecutors.keySet());
    }

    String assembleLongPollRefreshUrl(String uri, String namespace, String appId, Map<String, Long> notificationsMap) {
        Map<String, String> queryParams = Maps.newHashMap();
        queryParams.put("namespace", queryParamEscaper.escape(namespace));
        queryParams.put("appId", queryParamEscaper.escape(appId));
        queryParams
                .put("notifications", queryParamEscaper.escape(assembleNotifications(notificationsMap)));

        String localIp = NetUtil.getLocalHost();
        if (!Strings.isNullOrEmpty(localIp)) {
            queryParams.put("ip", queryParamEscaper.escape(localIp));
        }

        String params = MAP_JOINER.join(queryParams);
        if (!uri.endsWith("/")) {
            uri += "/";
        }

        return uri + "calibration?" + params;
    }

    String assembleNotifications(Map<String, Long> notificationsMap) {
        List<JediConfigNotification> notifications = Lists.newArrayList();
        for (Map.Entry<String, Long> entry : notificationsMap.entrySet()) {
            JediConfigNotification notification = new JediConfigNotification(entry.getKey(), entry.getValue());
            notifications.add(notification);
        }
        return gson.toJson(notifications);
    }
}
