package xyz.hellothomas.jedi.core.exception;

import xyz.hellothomas.jedi.core.enums.ICodeEnum;

import java.text.MessageFormat;

/**
 * @author 80234613 唐圆
 * @date 2021/7/8 16:53
 * @descripton
 * @version 1.0
 */
public class BusinessException extends RuntimeException {
    private final String code;

    public BusinessException(ICodeEnum codeEnum) {
        super(codeEnum.getMessage());
        this.code = codeEnum.getCode();
    }

    public BusinessException(ICodeEnum codeEnum, Object... params) {
        super(MessageFormat.format(codeEnum.getMessage(), params));
        this.code = codeEnum.getCode();
    }

    @Override
    public String toString() {
        return "BusinessException{ code=" + code + ", message=" + this.getMessage() + " }";
    }

    public String getCode() {
        return code;
    }
}
