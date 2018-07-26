package com.security.exception;

import org.springframework.http.HttpStatus;

public class PermisionException extends GlobalException {

    public PermisionException(String message, String error) {
        super(message, HttpStatus.FORBIDDEN, error);
    }

    public PermisionException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }

}
