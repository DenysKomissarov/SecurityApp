package com.security.app.exception;

import com.security.app.responce.ApiResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ApiResponse dataNotFound(Exception exception, WebRequest request){
        return new ApiResponse(false, exception.getMessage());
    }



}
