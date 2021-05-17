package xyz.hellothomas.jedi.core.enums;

import java.util.HashMap;
import java.util.Map;

public enum KafkaMessageTopic {
    TOPIC_EXECUTOR_TICKER(MessageType.EXECUTOR_TICKER, "executor-ticker"),
    TOPIC_EXECUTOR_TASK(MessageType.EXECUTOR_TASK, "executor-task"),
    TOPIC_EXECUTOR_SHUTDOWN(MessageType.EXECUTOR_SHUTDOWN, "executor-shutdown"),
    TOPIC_CUSTOM(null, "custom-notification"),
    ;

    private static final Map<MessageType, String> PATH_MAP = new HashMap<>();

    static {
        for (KafkaMessageTopic KafkaMessageTopic : KafkaMessageTopic.values()) {
            PATH_MAP.put(KafkaMessageTopic.messageType, KafkaMessageTopic.topic);
        }
    }

    private MessageType messageType;
    private String topic;

    KafkaMessageTopic(MessageType messageType, String topic) {
        this.messageType = messageType;
        this.topic = topic;
    }

    public static String getTopicByMessageType(MessageType messageType) {
        return PATH_MAP.get(messageType);
    }

    public String getTopic() {
        return topic;
    }
}
