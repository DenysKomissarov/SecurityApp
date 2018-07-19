package com.security.app.controller.advice;

import org.springframework.http.HttpStatus;

public class BadRequestException extends GlobalException {
    public BadRequestException(String message, String error) {
        super(message, HttpStatus.BAD_REQUEST, error);
    }
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}