package xyz.hellothomas.jedi.core.dto.consumer;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author Thomas
 * @date 2021/1/9 22:03
 * @description
 * @version 1.0
 */
public abstract class AbstractNotification {

    /**
     * 消息id
     */
    @NotBlank
    @Length(max = 36)
    private String id;

    /**
     * appId
     */
    @NotBlank
    @Length(max = 32)
    private String appId;

    /**
     * namespace
     */
    @NotBlank
    @Length(max = 32)
    private String namespace;

    /**
     * 消息类型
     */
    @NotBlank
    @Length(max = 32)
    private String messageType;

    /**
     * 消息生成时间
     */
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime recordTime;

    /**
     * 消息生成主机
     */
    @NotBlank
    @Length(max = 15)
    private String host;

    /**
     * 消息生成机器ID
     */
    @NotBlank
    @Length(max = 15)
    private String machineId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public LocalDateTime getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(LocalDateTime recordTime) {
        this.recordTime = recordTime;
    }

    @Override
    public String toString() {
        return "AbstractNotification(id=" + this.getId() + ", appId=" + this.getAppId() + ", namespace" +
                "=" + this.getNamespace() + ", messageType=" + this.getMessageType() + ", recordTime=" + this.getRecordTime() +
                ", host=" + this.getHost() + ", machineId=" + this.getMachineId() + ")";
    }
}
