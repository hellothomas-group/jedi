package xyz.hellothomas.jedi.config.application.message;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.hellothomas.jedi.biz.domain.ReleaseMessage;
import xyz.hellothomas.jedi.biz.infrastructure.mapper.ReleaseMessageMapper;
import xyz.hellothomas.jedi.core.utils.JediThreadFactory;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static xyz.hellothomas.jedi.biz.common.constants.Constants.JEDI_RELEASE_TOPIC;

@Slf4j
@Service
public class ReleaseMessageServiceWithCache implements ReleaseMessageListener, InitializingBean {
    private final ReleaseMessageMapper releaseMessageMapper;

    private int scanInterval;
    private TimeUnit scanIntervalTimeUnit;

    private volatile long maxIdScanned;

    private ConcurrentMap<String, ReleaseMessage> releaseMessageCache;

    private AtomicBoolean doScan;
    private ExecutorService executorService;

    public ReleaseMessageServiceWithCache(ReleaseMessageMapper releaseMessageMapper) {
        this.releaseMessageMapper = releaseMessageMapper;
        initialize();
    }

    private void initialize() {
        releaseMessageCache = Maps.newConcurrentMap();
        doScan = new AtomicBoolean(true);
        executorService = Executors.newSingleThreadExecutor(JediThreadFactory
                .create("ReleaseMessageServiceWithCache", true));
    }

    public List<ReleaseMessage> findLatestReleaseMessagesGroupByMessages(Set<String> messages) {
        if (CollectionUtils.isEmpty(messages)) {
            return Collections.emptyList();
        }
        List<ReleaseMessage> releaseMessages = Lists.newArrayList();

        for (String message : messages) {
            ReleaseMessage releaseMessage = releaseMessageCache.get(message);
            if (releaseMessage != null) {
                releaseMessages.add(releaseMessage);
            }
        }

        return releaseMessages;
    }

    @Override
    public void handleMessage(ReleaseMessage message, String channel) {
        //Could stop once the ReleaseMessageScanner starts to work
        doScan.set(false);
        log.info("message received - channel: {}, message: {}", channel, message);

        String content = message.getMessage();
        log.info("Jedi.ReleaseMessageService.UpdateCache {}", message.getId());
        if (!JEDI_RELEASE_TOPIC.equals(channel) || Strings.isNullOrEmpty(content)) {
            return;
        }

        long gap = message.getId() - maxIdScanned;
        if (gap == 1) {
            mergeReleaseMessage(message);
        } else if (gap > 1) {
            //gap found!
            loadReleaseMessages(maxIdScanned);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        populateDataBaseInterval();
        //block the startup process until load finished
        //this should happen before ReleaseMessageScanner due to autowire
        loadReleaseMessages(0);

        executorService.submit(() -> {
            while (doScan.get() && !Thread.currentThread().isInterrupted()) {
                try {
                    loadReleaseMessages(maxIdScanned);
                } catch (Throwable ex) {
                    log.error("Scan new release messages failed", ex);
                }

                try {
                    scanIntervalTimeUnit.sleep(scanInterval);
                } catch (InterruptedException e) {
                    //ignore
                }
            }
        });
    }

    private synchronized void mergeReleaseMessage(ReleaseMessage releaseMessage) {
        ReleaseMessage old = releaseMessageCache.get(releaseMessage.getMessage());
        if (old == null || releaseMessage.getId() > old.getId()) {
            releaseMessageCache.put(releaseMessage.getMessage(), releaseMessage);
            maxIdScanned = releaseMessage.getId();
        }
    }

    private void loadReleaseMessages(long startId) {
        boolean hasMore = true;
        while (hasMore && !Thread.currentThread().isInterrupted()) {
            //current batch is 500
            List<ReleaseMessage> releaseMessages = releaseMessageMapper
                    .findFirst500ByIdGreaterThanOrderByIdAsc(startId);
            if (CollectionUtils.isEmpty(releaseMessages)) {
                break;
            }
            releaseMessages.forEach(this::mergeReleaseMessage);
            int scanned = releaseMessages.size();
            startId = releaseMessages.get(scanned - 1).getId();
            hasMore = scanned == 500;
            log.info("Loaded {} release messages with startId {}", scanned, startId);
        }
    }

    private void populateDataBaseInterval() {
        scanInterval = 1;
        scanIntervalTimeUnit = TimeUnit.SECONDS;
    }

    //only for test use
    private void reset() throws Exception {
        executorService.shutdownNow();
        initialize();
        afterPropertiesSet();
    }
}
