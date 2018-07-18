package com.security.app.controller.advice;

import com.security.app.responce.ApiResponse;

import java.util.Map;

public class PermisionException extends GlobalException {

    private ApiResponse apiResponse;

    public PermisionException(String message, String error) {
        super(message, 403, error);
    }

    @Override
    public ApiResponse getApiResponse() {
        return apiResponse;
    }
}
