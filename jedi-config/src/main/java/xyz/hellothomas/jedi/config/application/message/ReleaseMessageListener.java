package xyz.hellothomas.jedi.config.application.message;

import xyz.hellothomas.jedi.biz.domain.ReleaseMessage;

/**
 * @author Thomas
 * @date 2021/3/11 22:48
 * @description
 * @version 1.0
 */
public interface ReleaseMessageListener {
    /**
     * 处理消息
     *
     * @param message
     * @param channel
     */
    void handleMessage(ReleaseMessage message, String channel);
}
