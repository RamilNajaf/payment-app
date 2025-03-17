package com.example.msaccount.exception;

import com.example.msaccount.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException noSuchElementException) {
        ErrorResponse error = new ErrorResponse(404, noSuchElementException.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}
