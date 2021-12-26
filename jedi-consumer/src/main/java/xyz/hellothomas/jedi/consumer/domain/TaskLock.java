package xyz.hellothomas.jedi.consumer.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author
 */
@Getter
@Setter
@ToString
public class TaskLock {
    /**
     * 自增Id
     */
    private Integer id;

    /**
     * 任务日期
     */
    private LocalDate taskDate;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 1: locked, 0: unlocked
     */
    private Boolean isLocked;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 版本号
     */
    private Integer version;
}
