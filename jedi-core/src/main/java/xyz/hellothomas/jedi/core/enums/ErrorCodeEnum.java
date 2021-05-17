package xyz.hellothomas.jedi.core.enums;

public enum ErrorCodeEnum implements ICodeEnum {
    JSON_SERIALIZE_ERROR("CODE001", "JSON序列化错误,异常为:{0}"),
    JSON_DESERIALIZE_ERROR("CODE002", "JSON反序列化错误,内容为{0},异常为:{1}"),
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
