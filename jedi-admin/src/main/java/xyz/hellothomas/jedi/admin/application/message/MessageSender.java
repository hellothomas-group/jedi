package xyz.hellothomas.jedi.admin.application.message;

/**
 * @author Thomas
 */
public interface MessageSender {
    void sendMessage(String message, String channel);
}
