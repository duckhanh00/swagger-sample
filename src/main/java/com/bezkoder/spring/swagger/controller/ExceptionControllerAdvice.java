package com.bezkoder.spring.swagger.controller;

import com.bezkoder.spring.swagger.model.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionControllerAdvice {
  @ExceptionHandler({MissingRequestHeaderException.class})
  public ResponseEntity<ErrorMessage> handleMissingRequestHeaderException(Throwable exception, HttpServletRequest request) {
    var errorMessage = new ErrorMessage(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), exception.getMessage(), request.getRequestURI());
    return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
  }
}
