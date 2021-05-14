package com.hellothomas.jedi.core.exception;

import com.hellothomas.jedi.core.enums.ICodeEnum;

import java.text.MessageFormat;

/**
 * @ClassName MyException
 * @Author 80234613
 * @Date 2019-7-7 15:22
 * @Descripton
 * @Version 1.0
 */
public class MyException extends RuntimeException {
    private final String code;

    public MyException(ICodeEnum codeEnum) {
        super(codeEnum.getMessage());
        this.code = codeEnum.getCode();
    }

    public MyException(ICodeEnum codeEnum, Object... params) {
        super(MessageFormat.format(codeEnum.getMessage(), params));
        this.code = codeEnum.getCode();
    }

    @Override
    public String toString() {
        return "MyException{ code=" + code + ", message=" + this.getMessage() + " }";
    }

    public String getCode() {
        return code;
    }


}
