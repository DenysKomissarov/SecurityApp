package com.security.app.controller.advice;

public class UserNotFoundException extends GlobalException {
    public UserNotFoundException(String message, String error) {
        super(message, 404, error);
    }
}
