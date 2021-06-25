package xyz.hellothomas.jedi.consumer.infrastructure.exception;


import org.springframework.http.HttpStatus;

public class BadRequestException extends AbstractJediHttpException {

    public BadRequestException(String str) {
        super(str);
        setHttpStatus(HttpStatus.BAD_REQUEST);
    }
}
