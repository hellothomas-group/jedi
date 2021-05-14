package com.hellothomas.jedi.core.dto.config;

public class JediConfigNotification {
    private String executorName;

    private long notificationId;

    //for json converter
    public JediConfigNotification() {
    }

    public JediConfigNotification(String executorName, long notificationId) {
        this.executorName = executorName;
        this.notificationId = notificationId;
    }

    public String getExecutorName() {
        return executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    public long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(long notificationId) {
        this.notificationId = notificationId;
    }

    @Override
    public String toString() {
        return "JediConfigNotification{" +
                "executorName='" + executorName + '\'' +
                ", notificationId='" + notificationId +
                '}';
    }
}
