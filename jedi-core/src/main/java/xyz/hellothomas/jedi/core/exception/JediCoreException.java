package xyz.hellothomas.jedi.core.exception;

import xyz.hellothomas.jedi.core.enums.ICodeEnum;

import java.text.MessageFormat;

/**
 * @author Thomas
 * @date 2019-7-7 15:22
 * @descripton
 * @version 1.0
 */
public class JediCoreException extends RuntimeException {
    private final String code;

    public JediCoreException(ICodeEnum codeEnum) {
        super(codeEnum.getMessage());
        this.code = codeEnum.getCode();
    }

    public JediCoreException(ICodeEnum codeEnum, Object... params) {
        super(MessageFormat.format(codeEnum.getMessage(), params));
        this.code = codeEnum.getCode();
    }

    @Override
    public String toString() {
        return "JediCoreException{ code=" + code + ", message=" + this.getMessage() + " }";
    }

    public String getCode() {
        return code;
    }
}
