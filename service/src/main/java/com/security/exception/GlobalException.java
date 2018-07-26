package com.security.exception;


import org.springframework.http.HttpStatus;

public class GlobalException extends RuntimeException {
    private HttpStatus responseCode;
    private String error;

    public GlobalException(HttpStatus responseCode) {
        super();
        this.responseCode = responseCode;
    }

    public GlobalException(String message, HttpStatus responseCode) {
        super(message);
        this.responseCode = responseCode;
    }

    public GlobalException(String message, HttpStatus responseCode, String error) {
        super(message);
        this.responseCode = responseCode;
        this.error = error;
    }

    public HttpStatus getResponseCode() {
        return responseCode;
    }

    public String getError() {
        return error;
    }
}
