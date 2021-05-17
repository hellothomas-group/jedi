package xyz.hellothomas.jedi.biz.infrastructure.exception;


import org.springframework.http.HttpStatus;

public class BadRequestException extends AbstractJediHttpException {

    public BadRequestException(String str) {
        super(str);
        setHttpStatus(HttpStatus.BAD_REQUEST);
    }
}
