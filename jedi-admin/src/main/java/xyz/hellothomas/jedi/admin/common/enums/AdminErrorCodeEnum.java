package xyz.hellothomas.jedi.admin.common.enums;

import xyz.hellothomas.jedi.core.enums.ICodeEnum;

public enum AdminErrorCodeEnum implements ICodeEnum {
    USER_NOT_EXIST("ADM0001", "用户不存在"),
    PASSWORD_INVALID("ADM0002", "密码错误"),
    TOKEN_UNAVAILABLE("ADM0003", "token过期"),
    TOKEN_IS_NULL("ADM0004", "token为空"),
    ;

    private String code;
    private String message;

    AdminErrorCodeEnum(String code, String message) {
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
