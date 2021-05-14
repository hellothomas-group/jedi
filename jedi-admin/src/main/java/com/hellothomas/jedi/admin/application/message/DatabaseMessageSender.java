package com.hellothomas.jedi.admin.application.message;

import com.google.common.collect.Queues;
import com.hellothomas.jedi.biz.domain.ReleaseMessage;
import com.hellothomas.jedi.biz.infrastructure.mapper.ReleaseMessageMapper;
import com.hellothomas.jedi.core.utils.JediThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.hellothomas.jedi.biz.common.constants.Constants.JEDI_RELEASE_TOPIC;

@Slf4j
@Component
public class DatabaseMessageSender implements MessageSender {
    private static final int CLEAN_QUEUE_MAX_SIZE = 100;
    private BlockingQueue<Long> toClean = Queues.newLinkedBlockingQueue(CLEAN_QUEUE_MAX_SIZE);
    private final ExecutorService cleanExecutorService;
    private final AtomicBoolean cleanStopped;

    private final ReleaseMessageMapper releaseMessageMapper;

    public DatabaseMessageSender(ReleaseMessageMapper releaseMessageMapper) {
        cleanExecutorService = Executors.newSingleThreadExecutor(JediThreadFactory.create("DatabaseMessageSender",
                true));
        cleanStopped = new AtomicBoolean(false);
        this.releaseMessageMapper = releaseMessageMapper;
    }

    @Override
    @Transactional
    public void sendMessage(String message, String channel) {
        log.info("Sending message {} to channel {}", message, channel);
        if (!Objects.equals(channel, JEDI_RELEASE_TOPIC)) {
            log.warn("Channel {} not supported by DatabaseMessageSender!", channel);
            return;
        }

        log.info("Jedi.AdminService.ReleaseMessage {}", message);
        try {
            ReleaseMessage newMessage = new ReleaseMessage(message);
            newMessage.setDataChangeLastTime(LocalDateTime.now());
            releaseMessageMapper.save(newMessage);
            toClean.offer(newMessage.getId());
        } catch (Exception ex) {
            log.error("Sending message to database failed", ex);
            throw ex;
        }
    }

    @PostConstruct
    private void initialize() {
        cleanExecutorService.submit(() -> {
            while (!cleanStopped.get() && !Thread.currentThread().isInterrupted()) {
                try {
                    Long rm = toClean.poll(1, TimeUnit.SECONDS);
                    if (rm != null) {
                        cleanMessage(rm);
                    } else {
                        TimeUnit.SECONDS.sleep(5);
                    }
                } catch (Exception ex) {
                    log.error("清理releaseMessage异常", ex);
                }
            }
        });
    }

    private void cleanMessage(Long id) {
        //double check in case the release message is rolled back
        ReleaseMessage releaseMessage = releaseMessageMapper.selectByPrimaryKey(id);
        if (releaseMessage == null) {
            return;
        }
        boolean hasMore = true;
        while (hasMore && !Thread.currentThread().isInterrupted()) {
            List<ReleaseMessage> messages = releaseMessageMapper.findFirst100ByMessageAndIdLessThanOrderByIdAsc(
                    releaseMessage.getMessage(), releaseMessage.getId());

            if (!messages.isEmpty()) {
                releaseMessageMapper.deleteAll(messages);
            }

            hasMore = messages.size() == 100;

            messages.forEach(toRemove -> log.info("ReleaseMessage.Clean.{} {}", toRemove.getMessage(),
                    toRemove.getId()));
        }
    }

    void stopClean() {
        cleanStopped.set(true);
    }
}
