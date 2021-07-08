package xyz.hellothomas.jedi.admin.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.hellothomas.jedi.core.enums.ErrorCodeEnum;
import xyz.hellothomas.jedi.core.enums.ICodeEnum;

/**
 * @author Thomas
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    @JsonProperty("code")
    private String code;

    @JsonProperty("data")
    private T data;

    @JsonProperty("message")
    private String message;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(ErrorCodeEnum.SUCCESS.getCode());
        response.setMessage(ErrorCodeEnum.SUCCESS.getMessage());
        response.setData(data);
        return response;
    }

    public static <T> ApiResponse<T> fail(T data, String code) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(data.toString());
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
}
