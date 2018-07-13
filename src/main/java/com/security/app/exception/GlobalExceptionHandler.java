package com.security.app.exception;

import com.security.app.responce.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler{

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ApiResponse dataNotFound(Exception exception, WebRequest request){
        logger.debug(exception.getMessage() + "!!!!!!");
        return new ApiResponse(false, exception.getMessage());
    }



}
