package com.security.app.controller.advice;


import com.security.app.responce.ApiResponse;

import java.util.HashMap;
import java.util.Map;

public class GlobalException extends RuntimeException {
    private int responseCode;
    private String error;
    private ApiResponse apiResponse;

    public GlobalException(int responseCode) {
        super();
        this.responseCode = responseCode;
    }

    public GlobalException(String message, Throwable throwable) {
        super(message, throwable);
        responseCode = 500;
    }

    public GlobalException(String message, int responseCode) {
        super(message);
        this.responseCode = responseCode;
    }

    public GlobalException(String message, int responseCode, String error) {
        super(message);
        this.responseCode = responseCode;
        this.error = error;
    }

    public GlobalException(String message, int responseCode, String error, Throwable throwable) {
        super(message, throwable);
        this.responseCode = responseCode;
        this.error = error;
    }

    public ApiResponse getApiResponse() {
        return apiResponse;
    }
}
