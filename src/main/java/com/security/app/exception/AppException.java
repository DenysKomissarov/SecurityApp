package com.security.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // FIXME You need to hold all exceptions in Controller Advice
public class AppException extends RuntimeException {
    public AppException(String message) {
        super(message);
    }
}
