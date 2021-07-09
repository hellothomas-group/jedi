package xyz.hellothomas.jedi.core.enums;

public enum ErrorCodeEnum implements ICodeEnum {
    /**
     * 操作成功
     */
    SUCCESS("CODE000", "操作成功"),

    /**
     * 操作失败
     */
    FAIL("CODE001", "操作失败"),

    /**
     * 系统异常
     */
    SYSTEM_ERROR("CODE002", "系统异常"),

    /**
     * 参数异常
     */
    PARAM_ERROR("CODE003", "参数异常"),
    TOKEN_UNAVAILABLE("CODE004", "token过期"),
    TOKEN_IS_NULL("CODE005", "token为空"),

    JSON_SERIALIZE_ERROR("CODE006", "JSON序列化错误,异常为:{0}"),
    JSON_DESERIALIZE_ERROR("CODE007", "JSON反序列化错误,内容为{0},异常为:{1}"),
    ;

    private String code;
    private String message;

    ErrorCodeEnum(String code, String message) {
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
