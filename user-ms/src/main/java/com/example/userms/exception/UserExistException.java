package com.example.userms.exception;

public class UserExistException extends RuntimeException {

    public UserExistException(String message) {
        super(message);
    }
}