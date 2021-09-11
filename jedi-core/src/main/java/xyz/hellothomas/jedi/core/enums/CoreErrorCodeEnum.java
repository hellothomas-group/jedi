package xyz.hellothomas.jedi.core.enums;

public enum CoreErrorCodeEnum implements ICodeEnum {
    // 公共错误码
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

    // core错误码
    JSON_SERIALIZE_ERROR("COR0000", "JSON序列化错误,异常为:{0}"),
    JSON_DESERIALIZE_ERROR("COR0001", "JSON反序列化错误,内容为{0},异常为:{1}"),
    CMM_JSON_DESERIALIZE_ERROR("COR0002", "通讯JSON反序列化异常"),
    CMM_FAIL("COR0003", "通讯失败"),
    CMM_ERROR("COR0004", "通讯异常"),
    ;

    private String code;
    private String message;

    CoreErrorCodeEnum(String code, String message) {
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
