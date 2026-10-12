
package com.example.najvecaLolcina.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageError> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageError(
                        "Validation failed",
                        errors.toString()
                ));
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<MessageError> handleInvalidRequestBody(
            HttpMessageNotReadableException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageError(
                        "Invalid request body",
                        "Check the JSON syntax and field values."
                ));
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<MessageError> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageError(
                        "Invalid parameter",
                        "Parameter '" + ex.getName()
                                + "' has an invalid value."
                ));
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<MessageError> handleUserNotFound(
            UserNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageError(
                        "User not found",
                        ex.getMessage()
                ));
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<MessageError> handleNoSuchElement(
            NoSuchElementException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageError(
                        "Resource not found",
                        ex.getMessage()
                ));
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MessageError> handleIllegalArgument(
            IllegalArgumentException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageError(
                        "Invalid argument",
                        ex.getMessage()
                ));
    }


    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<MessageError> handleIllegalState(
            IllegalStateException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MessageError(
                        "Operation conflicts with current state",
                        ex.getMessage()
                ));
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<MessageError> handleDataIntegrityViolation(
            DataIntegrityViolationException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MessageError(
                        "Data conflict",
                        "The request conflicts with existing data "
                                + "or a database constraint."
                ));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageError> handleUnexpectedException(
            Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageError(
                        "Internal server error",
                        "An unexpected error occurred."
                ));
    }
}
