package xyz.hellothomas.jedi.admin.application.message;

/**
 * @author 80234613
 */
public interface MessageSender {
    void sendMessage(String message, String channel);
}
