package com.hellothomas.jedi.core.internals.message.http;

import com.hellothomas.jedi.core.dto.ReturnT;
import com.hellothomas.jedi.core.dto.consumer.AbstractNotification;
import com.hellothomas.jedi.core.enums.HttpNotificationPath;
import com.hellothomas.jedi.core.enums.MessageType;
import com.hellothomas.jedi.core.internals.message.AbstractNotificationService;
import com.hellothomas.jedi.core.utils.MonitorRemotingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @className HttpNotificationService
 * @author Thomas
 * @date 2021/1/3 21:33
 * @description
 * @version 1.0
 */
public class HttpNotificationService extends AbstractNotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpNotificationService.class);

    /**
     * 服务端地址
     */
    private String messageServerUrl;

    /**
     * 缓冲打点消息队列
     */
    private LinkedBlockingQueue<AbstractNotification> executorTickerNotificationQueue;

    /**
     * 缓冲任务消息队列
     */
    private LinkedBlockingQueue<AbstractNotification> executorTaskNotificationQueue;

    /**
     * 缓冲默认消息队列
     */
    private LinkedBlockingQueue<AbstractNotification> defaultNotificationQueue;

    /**
     * 打点消息发送线程终止标志
     */
    private volatile boolean toStopTicker;

    /**
     * 任务消息发送线程终止标志
     */
    private volatile boolean toStopTask;

    /**
     * 默认消息发送线程终止标志
     */
    private volatile boolean toStopDefault;

    public HttpNotificationService(String messageServerUrl, String appId, String namespace) {
        super(appId, namespace);
        this.messageServerUrl = messageServerUrl;
        this.executorTickerNotificationQueue = new LinkedBlockingQueue<>(1000);
        this.executorTaskNotificationQueue = new LinkedBlockingQueue<>(5000);
        this.defaultNotificationQueue = new LinkedBlockingQueue<>(1000);
        startTickerMessageSendThread();
        startTaskMessageSendThread();
        startDefaultMessageSendThread();
    }

    @Override
    public void pushNotification(AbstractNotification notification) {
        try {
            if (MessageType.EXECUTOR_TASK.getTypeValue().equals(notification.getMessageType())) {
                executorTaskNotificationQueue.add(notification);
            } else if (MessageType.EXECUTOR_TICKER.getTypeValue().equals(notification.getMessageType())) {
                executorTickerNotificationQueue.add(notification);
            } else if (MessageType.EXECUTOR_SHUTDOWN.getTypeValue().equals(notification.getMessageType())) {
                send(notification, MessageType.EXECUTOR_SHUTDOWN);
            } else {
                defaultNotificationQueue.add(notification);
            }
        } catch (Exception e) {
            LOGGER.warn("消息{} push异常: {}", notification, e);
        }
    }

    @Override
    public void send(Object request, MessageType messageType) {
        String requestUrl = messageServerUrl + HttpNotificationPath.getPathByMessageType(messageType);
        LOGGER.trace("send to {}, message: {}", requestUrl, request);
        ReturnT<String> result = MonitorRemotingUtil.postBody(requestUrl, null, 3,
                request, String.class);
        LOGGER.trace("result: {}", result);
    }

    /**
     * 启动守护线程执行消息发送任务
     *
     * @author 80234613 唐圆
     * @date 2019-11-14
     * @param
     * @return void
     */
    private void startTickerMessageSendThread() {
        Thread messageSendThread = new Thread(() -> {
            List<AbstractNotification> abstractNotifications = new ArrayList<>(10);
            while (!toStopTicker) {
                try {
                    LOGGER.trace("获取打点消息发送队列中...");
                    abstractNotifications.add(executorTickerNotificationQueue.take());
                    executorTickerNotificationQueue.drainTo(abstractNotifications, 9);
                    send(abstractNotifications, MessageType.EXECUTOR_TICKER);
                } catch (Exception e) {
                    if (!toStopTicker) {
                        LOGGER.error("打点消息发送任务失败, 异常为: {}, 消息为: {}! ", e, abstractNotifications);
                    }
                } finally {
                    abstractNotifications.clear();
                }
            }
        });

        messageSendThread.setDaemon(true);
        messageSendThread.setName("打点消息发送线程");
        messageSendThread.start();
        LOGGER.debug("{}已启动", messageSendThread.getName());
    }

    /**
     * 启动守护线程执行任务消息发送任务
     *
     * @author 80234613 唐圆
     * @date 2019-11-14
     * @param
     * @return void
     */
    private void startTaskMessageSendThread() {
        Thread messageSendThread = new Thread(() -> {
            List<AbstractNotification> abstractNotifications = new ArrayList<>(10);
            while (!toStopTask) {
                try {
                    LOGGER.trace("获取任务消息发送队列中...");
                    abstractNotifications.add(executorTaskNotificationQueue.take());
                    executorTaskNotificationQueue.drainTo(abstractNotifications, 9);
                    send(abstractNotifications, MessageType.EXECUTOR_TASK);
                } catch (Exception e) {
                    if (!toStopTask) {
                        LOGGER.error("任务消息发送任务失败, 异常为: {}, 消息为: {}! ", e, abstractNotifications);
                    }
                } finally {
                    abstractNotifications.clear();
                }
            }
        });

        messageSendThread.setDaemon(true);
        messageSendThread.setName("任务消息发送线程");
        messageSendThread.start();
        LOGGER.debug("{}已启动", messageSendThread.getName());
    }

    /**
     * 启动守护线程执行默认消息发送任务
     *
     * @author 80234613 唐圆
     * @date 2019-11-14
     * @param
     * @return void
     */
    private void startDefaultMessageSendThread() {
        Thread messageSendThread = new Thread(() -> {
            List<AbstractNotification> abstractNotifications = new ArrayList<>(10);
            while (!toStopDefault) {
                try {
                    LOGGER.trace("获取默认消息发送队列中...");
                    abstractNotifications.add(defaultNotificationQueue.take());
                    defaultNotificationQueue.drainTo(abstractNotifications, 9);
                    send(abstractNotifications, null);
                } catch (Exception e) {
                    if (!toStopDefault) {
                        LOGGER.error("默认消息发送任务失败, 异常为: {}, 消息为: {}! ", e, abstractNotifications);
                    }
                } finally {
                    abstractNotifications.clear();
                }
            }
        });

        messageSendThread.setDaemon(true);
        messageSendThread.setName("默认消息发送线程");
        messageSendThread.start();
        LOGGER.debug("{}已启动", messageSendThread.getName());
    }
}
