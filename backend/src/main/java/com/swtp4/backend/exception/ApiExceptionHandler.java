package com.swtp4.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    // if some entity not has been found, the frontend receive a error response
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        CustomErrorResponse error = new CustomErrorResponse(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // if the status of an application cant be changed from one to another the frontend receives this error as reponse
    @ExceptionHandler(InvalidApplicationStateException.class)
    public ResponseEntity<CustomErrorResponse> handleInvalidApplicationStateException(InvalidApplicationStateException ex) {
        CustomErrorResponse error = new CustomErrorResponse(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
