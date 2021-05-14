package com.hellothomas.jedi.config.application.message;

import com.google.common.collect.Lists;
import com.hellothomas.jedi.biz.domain.ReleaseMessage;
import com.hellothomas.jedi.biz.infrastructure.mapper.ReleaseMessageMapper;
import com.hellothomas.jedi.core.utils.JediThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.hellothomas.jedi.biz.common.constants.Constants.JEDI_RELEASE_TOPIC;
import static com.hellothomas.jedi.config.common.constants.Constants.DEFAULT_RELEASE_MESSAGE_SCAN_INTERVAL_IN_MS;

@Slf4j
public class ReleaseMessageScanner implements InitializingBean {
    private final ReleaseMessageMapper releaseMessageMapper;
    private int databaseScanInterval;
    private List<ReleaseMessageListener> listeners;
    private ScheduledExecutorService executorService;
    private long maxIdScanned;

    public ReleaseMessageScanner(ReleaseMessageMapper releaseMessageMapper) {
        this.releaseMessageMapper = releaseMessageMapper;
        listeners = Lists.newCopyOnWriteArrayList();
        executorService = Executors.newScheduledThreadPool(1, JediThreadFactory
                .create("ReleaseMessageScanner", true));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        databaseScanInterval = DEFAULT_RELEASE_MESSAGE_SCAN_INTERVAL_IN_MS;
        maxIdScanned = loadLargestMessageId();
        executorService.scheduleWithFixedDelay(() -> {
            try {
                scanMessages();
            } catch (Throwable ex) {
                log.error("Scan and send message failed", ex);
            }
        }, databaseScanInterval, databaseScanInterval, TimeUnit.MILLISECONDS);

    }

    /**
     * add message listeners for release message
     * @param listener
     */
    public void addMessageListener(ReleaseMessageListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Scan messages, continue scanning until there is no more messages
     */
    private void scanMessages() {
        boolean hasMoreMessages = true;
        while (hasMoreMessages && !Thread.currentThread().isInterrupted()) {
            hasMoreMessages = scanAndSendMessages();
        }
    }

    /**
     * scan messages and send
     *
     * @return whether there are more messages
     */
    private boolean scanAndSendMessages() {
        //current batch is 500
        List<ReleaseMessage> releaseMessages =
                releaseMessageMapper.findFirst500ByIdGreaterThanOrderByIdAsc(maxIdScanned);
        if (CollectionUtils.isEmpty(releaseMessages)) {
            return false;
        }
        fireMessageScanned(releaseMessages);
        int messageScanned = releaseMessages.size();
        maxIdScanned = releaseMessages.get(messageScanned - 1).getId();
        return messageScanned == 500;
    }

    /**
     * find largest message id as the current start point
     * @return current largest message id
     */
    private long loadLargestMessageId() {
        ReleaseMessage releaseMessage = releaseMessageMapper.findTopByOrderByIdDesc();
        return releaseMessage == null ? 0 : releaseMessage.getId();
    }

    /**
     * Notify listeners with messages loaded
     * @param messages
     */
    private void fireMessageScanned(List<ReleaseMessage> messages) {
        for (ReleaseMessage message : messages) {
            for (ReleaseMessageListener listener : listeners) {
                try {
                    listener.handleMessage(message, JEDI_RELEASE_TOPIC);
                } catch (Throwable ex) {
                    log.error("Failed to invoke message listener {}", listener.getClass(), ex);
                }
            }
        }
    }
}
