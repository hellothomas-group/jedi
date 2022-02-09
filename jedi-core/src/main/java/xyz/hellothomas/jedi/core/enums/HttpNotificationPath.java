package xyz.hellothomas.jedi.core.enums;

import java.util.EnumMap;

public enum HttpNotificationPath {
    PATH_EXECUTOR_TICKER(MessageType.EXECUTOR_TICKER, "/notification/executor-ticker"),
    PATH_EXECUTOR_TASK(MessageType.EXECUTOR_TASK, "/notification/executor-task"),
    PATH_EXECUTOR_SHUTDOWN(MessageType.EXECUTOR_SHUTDOWN, "/notification/executor-shutdown"),
    PATH_CUSTOM(MessageType.CUSTOM_NOTIFICATION, "/notification/custom"),
    ;

    private static final EnumMap<MessageType, String> PATH_MAP = new EnumMap<>(MessageType.class);

    static {
        for (HttpNotificationPath httpNotificationPath : HttpNotificationPath.values()) {
            PATH_MAP.put(httpNotificationPath.messageType, httpNotificationPath.path);
        }
    }

    private MessageType messageType;
    private String path;

    HttpNotificationPath(MessageType messageType, String path) {
        this.messageType = messageType;
        this.path = path;
    }

    public static String getPathByMessageType(MessageType messageType) {
        return PATH_MAP.get(messageType);
    }

    public String getPath() {
        return path;
    }
}
