package com.codelitt.technical.exercise.controller.base;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class BaseRESTController {

  /**
   * Handle {@link MethodArgumentNotValidException} exceptions thrown by the application and return a
   * map with field names as keys and error messages as values.
   *
   * @param ex the exception to handle
   * @return a map containing field names as keys and error messages as values
   * @throws {@link MethodArgumentNotValidException}
   * @ResponseStatus The HTTP status code is set to 400 (BAD_REQUEST)
   * @ExceptionHandler Specifies the type of exception this method handles.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected Map<String, String> handleValidationException(final MethodArgumentNotValidException ex) {
    final Map<String, String> errors = new HashMap<>();

    ex.getBindingResult().getAllErrors().forEach(error -> {
      var fieldName = ((FieldError) error).getField();
      var errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    return errors;
  }
}
