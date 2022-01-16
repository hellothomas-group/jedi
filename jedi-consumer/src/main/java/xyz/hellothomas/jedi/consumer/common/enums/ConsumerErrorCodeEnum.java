package xyz.hellothomas.jedi.consumer.common.enums;

import xyz.hellothomas.jedi.core.enums.ICodeEnum;

public enum ConsumerErrorCodeEnum implements ICodeEnum {
    TASK_NOT_EXIST("CSM0001", "任务不存在"),
    ;

    private String code;
    private String message;

    ConsumerErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }


}
