package xyz.hellothomas.jedi.config.api;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import xyz.hellothomas.jedi.biz.common.utils.ReleaseMessageKeyGenerator;
import xyz.hellothomas.jedi.biz.domain.ReleaseMessage;
import xyz.hellothomas.jedi.biz.infrastructure.exception.BadRequestException;
import xyz.hellothomas.jedi.config.application.message.DeferredResultWrapper;
import xyz.hellothomas.jedi.config.application.message.ReleaseMessageListener;
import xyz.hellothomas.jedi.config.application.message.ReleaseMessageServiceWithCache;
import xyz.hellothomas.jedi.core.dto.config.JediConfigNotification;
import xyz.hellothomas.jedi.core.utils.JediThreadFactory;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static xyz.hellothomas.jedi.biz.common.constants.Constants.JEDI_RELEASE_TOPIC;
import static xyz.hellothomas.jedi.config.common.constants.Constants.*;
import static xyz.hellothomas.jedi.core.constants.Constants.NAMESPACE_EXECUTOR_SEPARATOR;
import static xyz.hellothomas.jedi.core.constants.Constants.NOTIFICATION_ID_PLACEHOLDER;

/**
 * @author
 * @date 2021/2/1 22:36Thomas
 * @description 配置校准
 * @version 1.0
 */
@Api(value = "calibration", tags = "calibration")
@RestController
@RequestMapping("/calibration")
@Slf4j
public class CalibrationController implements ReleaseMessageListener {
    private static final Splitter STRING_SPLITTER = Splitter.on(NAMESPACE_EXECUTOR_SEPARATOR).omitEmptyStrings();
    private static final Type NOTIFICATIONS_TYPE_REFERENCE = new TypeToken<List<JediConfigNotification>>() {
    }.getType();

    private final Multimap<String, DeferredResultWrapper> deferredResults =
            Multimaps.synchronizedSetMultimap(TreeMultimap.create(String.CASE_INSENSITIVE_ORDER, Ordering.natural()));

    private final ExecutorService largeNotificationBatchExecutorService;
    private final ReleaseMessageServiceWithCache releaseMessageService;
    private final Gson gson;

    public CalibrationController(
            final ReleaseMessageServiceWithCache releaseMessageService, Gson gson) {
        largeNotificationBatchExecutorService = Executors.newSingleThreadExecutor(JediThreadFactory.create
                ("NotificationControllerV2", true));
        this.releaseMessageService = releaseMessageService;
        this.gson = gson;
    }

    @GetMapping
    public DeferredResult<ResponseEntity<List<JediConfigNotification>>> pollNotification(
            @RequestParam(value = "namespace") String namespace,
            @RequestParam(value = "appId") String appId,
            @RequestParam(value = "notifications") String notificationsAsString,
            @RequestParam(value = "ip", required = false) String clientIp) {
        List<JediConfigNotification> notifications = null;

        try {
            notifications =
                    gson.fromJson(notificationsAsString, NOTIFICATIONS_TYPE_REFERENCE);
        } catch (Exception ex) {
            log.error("反序列化notifications异常:{}", ex);
        }

        if (CollectionUtils.isEmpty(notifications)) {
            throw new BadRequestException("Invalid format of notifications: " + notificationsAsString);
        }

        DeferredResultWrapper deferredResultWrapper = new DeferredResultWrapper(DEFAULT_LONG_POLLING_TIMEOUT);
        // 客户端key
        Set<String> executorNames = Sets.newHashSetWithExpectedSize(notifications.size());
        // 客户端版本
        Map<String, Long> clientSideNotifications = Maps.newHashMapWithExpectedSize(notifications.size());
        // 客户端和服务端key映射关系
        Map<String, String> watchedKeysMap = Maps.newHashMapWithExpectedSize(notifications.size());
        notifications.stream().forEach(i -> {
            executorNames.add(i.getExecutorName());
            clientSideNotifications.put(i.getExecutorName(), i.getNotificationId());
            watchedKeysMap.put(i.getExecutorName(), ReleaseMessageKeyGenerator.generate(namespace, appId,
                    i.getExecutorName()));
        });
        Set<String> watchedKeys = Sets.newHashSet(watchedKeysMap.values());

        /**
         * 1、set deferredResult before the check, for avoid more waiting
         * If the check before setting deferredResult,it may receive a notification the next time
         * when method handleMessage is executed between check and set deferredResult.
         */
        deferredResultWrapper
                .onTimeout(() -> log.info("Jedi.LongPoll.TimeOutKeys:{}", watchedKeys));

        deferredResultWrapper.onCompletion(() -> {
            //unregister all keys
            watchedKeys.stream().forEach(i -> deferredResults.remove(i, deferredResultWrapper));
            log.info("Jedi.LongPoll.CompletedKeys:{}", watchedKeys);
        });

        //register all keys
        watchedKeys.stream().forEach(i -> this.deferredResults.put(i, deferredResultWrapper));
        log.info("Jedi.LongPoll.RegisteredKeys:{}", watchedKeys);
        log.debug("Listening {} from appId: {}, namespace: {}", watchedKeys, appId, namespace);

        /**
         * 2、check new release
         */
        List<ReleaseMessage> latestReleaseMessages =
                releaseMessageService.findLatestReleaseMessagesGroupByMessages(watchedKeys);

        List<JediConfigNotification> newNotifications =
                getJediConfigNotifications(executorNames, clientSideNotifications, watchedKeysMap,
                        latestReleaseMessages);

        if (!CollectionUtils.isEmpty(newNotifications)) {
            deferredResultWrapper.setResult(newNotifications);
        }

        return deferredResultWrapper.getResult();
    }

    private List<JediConfigNotification> getJediConfigNotifications(Set<String> executorNames,
                                                                    Map<String, Long> clientSideNotifications,
                                                                    Map<String, String> watchedKeysMap,
                                                                    List<ReleaseMessage> latestReleaseMessages) {
        List<JediConfigNotification> newNotifications = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(latestReleaseMessages)) {
            Map<String, Long> latestNotifications = Maps.newHashMap();
            for (ReleaseMessage releaseMessage : latestReleaseMessages) {
                latestNotifications.put(releaseMessage.getMessage(), releaseMessage.getId());
            }

            for (String executorName : executorNames) {
                long clientSideId = clientSideNotifications.get(executorName);
                String executorWatchedKey = watchedKeysMap.get(executorName);
                long latestId = latestNotifications.getOrDefault(executorWatchedKey, NOTIFICATION_ID_PLACEHOLDER);
                if (latestId > clientSideId) {
                    JediConfigNotification notification = new JediConfigNotification(executorName, latestId);
                    newNotifications.add(notification);
                }
            }
        }
        return newNotifications;
    }

    @Override
    public void handleMessage(ReleaseMessage message, String channel) {
        log.info("message received - channel: {}, message: {}", channel, message);

        String content = message.getMessage();
        if (!JEDI_RELEASE_TOPIC.equals(channel) || Strings.isNullOrEmpty(content)) {
            return;
        }

        String changedExecutor = retrieveExecutorFromReleaseMessage.apply(content);

        if (Strings.isNullOrEmpty(changedExecutor)) {
            log.error("message format invalid - {}", content);
            return;
        }

        if (!deferredResults.containsKey(content)) {
            return;
        }

        //create a new list to avoid ConcurrentModificationException
        List<DeferredResultWrapper> results = Lists.newArrayList(deferredResults.get(content));

        JediConfigNotification configNotification = new JediConfigNotification(changedExecutor, message.getId());

        //do async notification if too many clients
        if (results.size() > DEFAULT_RELEASE_MESSAGE_NOTIFICATION_BATCH) {
            largeNotificationBatchExecutorService.submit(() -> {
                log.debug("Async notify {} clients for key {} with batch {}", results.size(), content,
                        DEFAULT_RELEASE_MESSAGE_NOTIFICATION_BATCH);
                for (int i = 0; i < results.size(); i++) {
                    if (i > 0 && i % DEFAULT_RELEASE_MESSAGE_NOTIFICATION_BATCH == 0) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(DEFAULT_RELEASE_MESSAGE_NOTIFICATION_BATCH_INTERVAL_IN_MILLI);
                        } catch (InterruptedException e) {
                            //ignore
                        }
                    }
                    log.debug("Async notify {}", results.get(i));
                    results.get(i).setResult(configNotification);
                }
            });
            return;
        }

        log.debug("Notify {} clients for key {}", results.size(), content);

        for (DeferredResultWrapper result : results) {
            result.setResult(configNotification);
        }
        log.debug("Notification completed");
    }

    private static final Function<String, String> retrieveExecutorFromReleaseMessage =
            releaseMessage -> {
                if (Strings.isNullOrEmpty(releaseMessage)) {
                    return null;
                }
                List<String> keys = STRING_SPLITTER.splitToList(releaseMessage);
                //message should be appId+namespace+executor
                if (keys.size() != 3) {
                    log.error("message format invalid - {}", releaseMessage);
                    return null;
                }
                return keys.get(2);
            };
}
