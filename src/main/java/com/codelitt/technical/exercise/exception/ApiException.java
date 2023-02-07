package com.codelitt.technical.exercise.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class ApiException extends Exception {

  @Serial
  private static final long serialVersionUID = 1L;

  private final int statusCode;

  public ApiException() {
    super();
    this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
  }

  public ApiException(String message) {
    super(message);
    this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
  }

  public ApiException(String message, Throwable cause) {
    super(message, cause);
    this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
  }

  public ApiException(Throwable cause) {
    super(cause);
    this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
  }

  public ApiException(int statusCode, String message) {
    super(message);
    this.statusCode = statusCode;
  }

  public ApiException(int statusCode, String message, Throwable cause) {
    super(message, cause);
    this.statusCode = statusCode;
  }

  public int getStatusCode() {
    return statusCode;
  }
}

