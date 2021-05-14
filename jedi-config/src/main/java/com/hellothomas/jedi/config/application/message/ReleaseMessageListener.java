package com.hellothomas.jedi.config.application.message;

import com.hellothomas.jedi.biz.domain.ReleaseMessage;

/**
 * @classname ReleaseMessageListener
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
