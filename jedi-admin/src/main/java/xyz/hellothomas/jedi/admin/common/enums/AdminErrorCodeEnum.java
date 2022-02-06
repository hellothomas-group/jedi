package xyz.hellothomas.jedi.admin.common.enums;

import xyz.hellothomas.jedi.core.enums.ICodeEnum;

public enum AdminErrorCodeEnum implements ICodeEnum {
    USER_NOT_EXIST("ADM0001", "用户不存在"),
    PASSWORD_INVALID("ADM0002", "密码错误"),
    TOKEN_UNAVAILABLE("ADM0003", "token过期"),
    TOKEN_IS_NULL("ADM0004", "token为空"),
    AUTHORIZATION_NOT_PASS("ADM0005", "无操作权限"),
    EXECUTOR_INVALID("ADM0006", "线程池名称非法"),
    APP_EXIST("ADM0007", "namespace app already exist."),
    APP_REQUEST_ERROR("ADM0008", "The namespace name of path variable and request body is different"),
    APP_NOT_EXIST("ADM0009", "namespace app not found"),
    EXECUTOR_EXIST("ADM0010", "executor already exist."),
    APP_INVALID("ADM0011", "应用名称非法"),
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
