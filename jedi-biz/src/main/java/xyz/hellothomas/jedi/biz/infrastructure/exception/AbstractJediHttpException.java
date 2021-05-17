package xyz.hellothomas.jedi.biz.infrastructure.exception;

import org.springframework.http.HttpStatus;

public abstract class AbstractJediHttpException extends RuntimeException {

    private static final long serialVersionUID = -1713129594004951820L;

    protected HttpStatus httpStatus;

    public AbstractJediHttpException(String msg) {
        super(msg);
    }

    public AbstractJediHttpException(String msg, Exception e) {
        super(msg, e);
    }

    protected void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
