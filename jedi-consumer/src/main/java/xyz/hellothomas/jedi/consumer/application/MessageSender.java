package xyz.hellothomas.jedi.consumer.application;

import java.util.List;

/**
 * @author 80234613 唐圆
 * @date 2021/6/28 14:59
 * @descripton
 * @version 1.0
 */
public interface MessageSender {
    /**
     * 消息通知
     *
     * @param users
     * @param msg
     */
    void notify(List<String> users, String msg);
}
