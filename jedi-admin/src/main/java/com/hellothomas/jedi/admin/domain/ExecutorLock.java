package com.hellothomas.jedi.admin.domain;

import com.hellothomas.jedi.biz.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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