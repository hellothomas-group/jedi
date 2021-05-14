package com.hellothomas.jedi.admin.common.enums;

import lombok.Getter;

/**
 * @author Thomas
 */

@Getter
public enum ReleaseOperationEnum {
    NORMAL_RELEASE(0),
    ROLLBACK(1),
    ;

    private int value;

    ReleaseOperationEnum(int value) {
        this.value = value;
    }
}
