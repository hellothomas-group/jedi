package xyz.hellothomas.jedi.core.dto.consumer;

import org.hibernate.validator.constraints.Length;

/**
 * @author Thomas
 * @date 2021/3/7 20:35
 * @description
 * @version 1.0
 */
public class CustomNotification extends AbstractNotification {
    /**
     * 消息内容
     */
    @Length(max = 4096)
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CustomNotification(id=" + this.getId() + ", appId=" + this.getAppId() + ", namespace" +
                "=" + this.getNamespace() + ", messageType=" + this.getMessageType() + ", content=" +
                this.getContent() + ", recordTime=" + this.getRecordTime() + ", host=" + this.getHost() + ")";
    }
}
