package com.hellothomas.jedi.config.application.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.hellothomas.jedi.biz.common.utils.ReleaseMessageKeyGenerator;
import com.hellothomas.jedi.biz.domain.Release;
import com.hellothomas.jedi.biz.domain.ReleaseMessage;
import com.hellothomas.jedi.config.application.ReleaseMessageService;
import com.hellothomas.jedi.config.application.ReleaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.hellothomas.jedi.biz.common.constants.Constants.JEDI_RELEASE_TOPIC;
import static com.hellothomas.jedi.core.constants.Constants.NAMESPACE_EXECUTOR_SEPARATOR;
import static com.hellothomas.jedi.core.constants.Constants.NOTIFICATION_ID_PLACEHOLDER;

/**
 * config service with caffeine cache
 */
@Slf4j
public class ConfigServiceWithCache extends AbstractConfigService {
    private static final long DEFAULT_EXPIRED_AFTER_ACCESS_IN_MINUTES = 60;//1 hour
    private static final Splitter STRING_SPLITTER =
            Splitter.on(NAMESPACE_EXECUTOR_SEPARATOR).omitEmptyStrings();

    @Autowired
    private ReleaseService releaseService;

    @Autowired
    private ReleaseMessageService releaseMessageService;

    private LoadingCache<String, ConfigCacheEntry> configCache;

    private ConfigCacheEntry nullConfigCacheEntry;

    public ConfigServiceWithCache() {
        nullConfigCacheEntry = new ConfigCacheEntry(NOTIFICATION_ID_PLACEHOLDER, null);
    }

    @PostConstruct
    void initialize() {
        configCache = Caffeine.newBuilder()
                .expireAfterAccess(DEFAULT_EXPIRED_AFTER_ACCESS_IN_MINUTES, TimeUnit.MINUTES)
                .build(key -> {
                    List<String> executorInfo = STRING_SPLITTER.splitToList(key);
                    if (executorInfo.size() != 3) {
                        log.error("Invalid cache load key {}", key);
                        return nullConfigCacheEntry;
                    }

                    log.debug("doing ConfigCache.LoadFromDB key:{}", key);

                    try {
                        ReleaseMessage latestReleaseMessage =
                                releaseMessageService.findLatestReleaseMessageForMessages(Lists
                                        .newArrayList(key));
                        Release latestRelease = releaseService.findLatestActiveRelease(executorInfo.get(0),
                                executorInfo.get(1),
                                executorInfo.get(2));

                        log.debug("done ConfigCache.LoadFromDB key:{}", key);

                        long notificationId = latestReleaseMessage == null ? NOTIFICATION_ID_PLACEHOLDER :
                                latestReleaseMessage
                                        .getId();

                        if (notificationId == NOTIFICATION_ID_PLACEHOLDER && latestRelease == null) {
                            return nullConfigCacheEntry;
                        }

                        return new ConfigCacheEntry(notificationId, latestRelease);
                    } catch (Throwable ex) {
                        log.error("ConfigCache.LoadFromDB error:{}", ex);
                        throw ex;
                    }
                });
    }

    @Override
    protected Release findLatestActiveRelease(String namespaceName, String appId, String executorName,
                                              long notificationId) {
        String key = ReleaseMessageKeyGenerator.generate(namespaceName, appId, executorName);

        log.debug("ConfigCache.Get {}", key);

        ConfigCacheEntry cacheEntry = configCache.get(key);

        //cache is out-dated
        if (notificationId > cacheEntry.getNotificationId()) {
            //invalidate the cache and try to load from db again
            invalidate(key);
            cacheEntry = configCache.get(key);
        }

        return cacheEntry.getRelease();
    }

    private void invalidate(String key) {
        configCache.invalidate(key);
        log.debug("ConfigCache.Invalidate {}", key);
    }

    @Override
    public void handleMessage(ReleaseMessage message, String channel) {
        log.info("message received - channel: {}, message: {}", channel, message);
        if (!JEDI_RELEASE_TOPIC.equals(channel) || Strings.isNullOrEmpty(message.getMessage())) {
            return;
        }

        try {
            invalidate(message.getMessage());

            //warm up the cache
            configCache.get(message.getMessage());
        } catch (Throwable ex) {
            //ignore
        }
    }

    private static class ConfigCacheEntry {
        private final long notificationId;
        private final Release release;

        public ConfigCacheEntry(long notificationId, Release release) {
            this.notificationId = notificationId;
            this.release = release;
        }

        public long getNotificationId() {
            return notificationId;
        }

        public Release getRelease() {
            return release;
        }
    }
}
