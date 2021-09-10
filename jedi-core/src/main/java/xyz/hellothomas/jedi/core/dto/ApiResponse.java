package xyz.hellothomas.jedi.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import xyz.hellothomas.jedi.core.enums.ErrorCodeEnum;
import xyz.hellothomas.jedi.core.enums.ICodeEnum;

/**
 * @author Thomas
 */
public class ApiResponse<T> {
    @JsonProperty("code")
    private String code;

    @JsonProperty("data")
    private T data;

    @JsonProperty("message")
    private String message;

    public ApiResponse() {
    }

    public ApiResponse(String code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(ErrorCodeEnum.SUCCESS.getCode());
        response.setMessage(ErrorCodeEnum.SUCCESS.getMessage());
        response.setData(data);
        return response;
    }

    public static <T> ApiResponse<T> fail(T msg, String code) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(msg.toString());
        return response;
    }

    public static ApiResponse fail(String msg) {
        ApiResponse response = new ApiResponse<>();
        response.setCode(ErrorCodeEnum.FAIL.getCode());
        response.setMessage(msg);
        return response;
    }

    public static <T> ApiResponse<T> fail(ICodeEnum errorCodeEnum) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(errorCodeEnum.getCode());
        response.setMessage(errorCodeEnum.getMessage());
        return response;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "code='" + code + '\'' +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
