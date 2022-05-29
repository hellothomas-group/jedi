package xyz.hellothomas.jedi.collector.common.enums;

import xyz.hellothomas.jedi.core.enums.ICodeEnum;

public enum CollectorErrorCodeEnum implements ICodeEnum {
    TASK_NOT_EXIST("CSM0001", "任务不存在"),
    ;

    private String code;
    private String message;

    CollectorErrorCodeEnum(String code, String message) {
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
