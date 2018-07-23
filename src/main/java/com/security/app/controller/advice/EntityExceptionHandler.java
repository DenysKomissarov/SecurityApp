package com.security.app.controller.advice;

import com.security.app.exception.GlobalException;
import com.security.app.responce.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class EntityExceptionHandler{

    private static final Logger logger = LoggerFactory.getLogger(EntityExceptionHandler.class);

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ApiResponse> globalHandler(GlobalException exception){
        logger.info(exception.getResponseCode() + ", " + exception.getMessage());
        return new ResponseEntity<>(new ApiResponse(exception.getResponseCode(), exception.getMessage()),
                exception.getResponseCode());
    }


}
