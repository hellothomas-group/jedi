package com.hellothomas.jedi.biz.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends AbstractJediHttpException {


  public NotFoundException(String str) {
    super(str);
    setHttpStatus(HttpStatus.NOT_FOUND);
  }
}
