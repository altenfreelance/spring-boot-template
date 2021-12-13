package com.alten.template.exception;

import com.alten.template.model.GenericErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(ExampleException.class)
    public ResponseEntity<GenericErrorResponse> handleTestException(ExampleException exception){
        return new ResponseEntity<>(new GenericErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }
}