package xyz.hellothomas.jedi.consumer.application;

import xyz.hellothomas.jedi.core.dto.consumer.AbstractNotification;

/**
 * @author Thomas
 * @date 2021/6/23 22:28
 * @description
 * @version 1.0
 */
public interface NotificationService<T extends AbstractNotification> {

    /**
     * 保存通知
     *
     * @param notification
     */
    void process(T notification);

    /**
     * 通知匹配处理类
     *
     * @param notification
     * @return
     */
    boolean match(T notification);
}
