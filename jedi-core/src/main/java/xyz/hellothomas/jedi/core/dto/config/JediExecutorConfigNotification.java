package xyz.hellothomas.jedi.core.dto.config;

public class JediExecutorConfigNotification {
    private String executorName;

    private long notificationId;

    //for json converter
    public JediExecutorConfigNotification() {
    }

    public JediExecutorConfigNotification(String executorName, long notificationId) {
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
        return "JediExecutorConfigNotification{" +
                "executorName='" + executorName + '\'' +
                ", notificationId='" + notificationId +
                '}';
    }
}
