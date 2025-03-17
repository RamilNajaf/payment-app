package com.example.mspayment.exception;

import com.example.mspayment.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        ErrorResponse error = new ErrorResponse(400);

        BindingResult result = exception.getBindingResult();
        Map<String, String> map = new HashMap<>();

        result.getFieldErrors().forEach(fieldError -> map.put(fieldError.getField(), fieldError.getDefaultMessage()));
        error.setValidationErr(map);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
