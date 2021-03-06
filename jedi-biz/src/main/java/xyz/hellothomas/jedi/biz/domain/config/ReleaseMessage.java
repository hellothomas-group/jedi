package xyz.hellothomas.jedi.biz.domain.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author Thomas
 * @date 2021/3/11 22:51
 * @description
 * @version 1.0
 */
@Getter
@Setter
@ToString
public class ReleaseMessage {

    /**
     * 自增主键
     */
    private Long id;

    /**
     * 发布的消息内容
     */
    private String message;

    /**
     * 最后修改时间
     */
    private LocalDateTime updateTime;

    public ReleaseMessage() {
    }

    public ReleaseMessage(String message) {
        this.message = message;
    }
}
