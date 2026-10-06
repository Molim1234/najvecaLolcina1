package com.example.najvecaLolcina.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Validation errors:
    // @NotBlank, @NotNull, @Size, @Email...
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageError> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        MessageError messageError = new MessageError(
                "Validation failed",
                errors.toString()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(messageError);
    }


    // User not found
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<MessageError> handleUserNotFound(
            UserNotFoundException ex) {

        MessageError messageError = new MessageError(
                "User not found",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(messageError);
    }


    // Illegal argument
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MessageError> handleIllegalArgument(
            IllegalArgumentException ex) {

        MessageError messageError = new MessageError(
                "Invalid argument",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(messageError);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<MessageError> handleNoSuchElement(NoSuchElementException ex) {
        MessageError messageError = new MessageError(
                "Resource not found",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // Vratiće lepo 404 umesto 500
                .body(messageError);
    }


    // Any unexpected exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(
            Exception ex) {

        String error = "Internal server error";

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}