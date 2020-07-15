package com.demo.controller.exception;

import com.demo.model.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorHandlerAdvice {
    @Autowired
    private ErrorResponseBuilder errorResponseBuilder;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errorCodes = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        return buildResponseEntity(errorCodes);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> errorCodes = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        return buildResponseEntity(errorCodes);
    }

    private ResponseEntity<ErrorResponse> buildResponseEntity(List<String> errorsString) {
        ErrorResponse errorResponse = errorResponseBuilder.build(errorsString);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
