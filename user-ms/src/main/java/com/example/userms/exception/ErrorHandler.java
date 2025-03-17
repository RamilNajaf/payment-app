package com.example.userms.exception;

import com.example.userms.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        ErrorResponse error = new ErrorResponse(401,illegalArgumentException.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handeUserExistException(UserExistException userExistException) {
        ErrorResponse error = new ErrorResponse(409,userExistException.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse>  handleNoSuchElementException(NoSuchElementException noSuchMethodException) {
        ErrorResponse error = new ErrorResponse(404,noSuchMethodException.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

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