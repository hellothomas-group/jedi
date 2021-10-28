package xyz.hellothomas.jedi.consumer.infrastructure.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.hellothomas.jedi.core.dto.ApiResponse;
import xyz.hellothomas.jedi.core.enums.CoreErrorCodeEnum;
import xyz.hellothomas.jedi.core.exception.BusinessException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * @className ExceptionHandlerAdvice
 * @author 80234613 唐圆
 * @date 2020/12/29 19:01
 * @descripton
 * @version 1.0
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ApiResponse businessHandler(BusinessException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.fail(e.getMessage(), e.getCode());
    }

    @ExceptionHandler({
            BindException.class,
            HttpMessageNotReadableException.class,
            ConstraintViolationException.class})
    @ResponseBody
    public ApiResponse paramsHandler(Exception e) {
        log.error("传参错误", e);
        return ApiResponse.fail(CoreErrorCodeEnum.PARAM_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiResponse handleException(HttpServletRequest request,
                                       MethodArgumentNotValidException exception) {
        log.error("handleException: ", exception);
        String message = String.join(",", exception.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ":" + e.getDefaultMessage())
                .collect(Collectors.toList()));
        log.error("handleError: " + message);
        return ApiResponse.fail(message, CoreErrorCodeEnum.PARAM_ERROR.getCode());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiResponse defaultErrorHandler(Exception e) {
        log.error("default error handler", e);
        return ApiResponse.fail(CoreErrorCodeEnum.SYSTEM_ERROR);
    }
}
