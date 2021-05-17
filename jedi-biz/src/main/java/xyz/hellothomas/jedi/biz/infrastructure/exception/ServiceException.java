package xyz.hellothomas.jedi.biz.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class ServiceException extends AbstractJediHttpException {

  public ServiceException(String str) {
    super(str);
    setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public ServiceException(String str, Exception e) {
    super(str, e);
    setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
