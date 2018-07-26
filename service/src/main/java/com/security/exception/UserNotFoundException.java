package com.security.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends GlobalException {
    public UserNotFoundException(String message, String error) {
        super(message, HttpStatus.NOT_FOUND, error);
    }
}
