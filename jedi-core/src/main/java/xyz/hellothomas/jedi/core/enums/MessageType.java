package xyz.hellothomas.jedi.core.enums;

import java.util.Arrays;

/**
 * @author Thomas
 * @date 2021/1/9 22:13
 * @description
 * @version 1.0
 */
public enum MessageType {
    EXECUTOR_TICKER("executor-ticker", "线程池打点消息"),
    EXECUTOR_TASK("executor-task", "线程池任务消息"),
    EXECUTOR_SHUTDOWN("executor-shutdown", "线程池关闭消息"),
    CUSTOM_NOTIFICATION("custom-notification", "自定义消息"),
    ;

    private String typeValue;
    private String typeName;

    MessageType(String typeValue, String typeName) {
        this.typeValue = typeValue;
        this.typeName = typeName;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public static MessageType getMessageType(String typeValue) {
        if (typeValue == null) {
            return null;
        }

        return Arrays.stream(MessageType.values()).filter(i -> i.getTypeValue().equals(typeValue)).findFirst().orElse(null);
    }
}
