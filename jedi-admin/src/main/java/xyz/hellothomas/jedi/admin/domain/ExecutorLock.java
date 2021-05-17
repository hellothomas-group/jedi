package xyz.hellothomas.jedi.admin.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import xyz.hellothomas.jedi.biz.domain.BaseEntity;

/**
 * @author
 */
@Getter
@Setter
@ToString
public class ExecutorLock extends BaseEntity {

    /**
     * executorId
     */
    private Long executorId;
}