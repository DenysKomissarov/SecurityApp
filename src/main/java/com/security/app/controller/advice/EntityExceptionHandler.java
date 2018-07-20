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

   /* @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiResponse userNotFound(UserNotFoundException ex){
        logger.info(ex.getMessage());
        return new ApiResponse(ex.getResponseCode(), ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResponse badRequest(BadRequestException ex){
        logger.info(ex.getMessage());
        return new ApiResponse(ex.getResponseCode(), ex.getMessage());
    }

    @ExceptionHandler(PermisionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    protected ApiResponse permision(PermisionException ex) {
        logger.info(ex.getMessage());
        return new ApiResponse(ex.getResponseCode(), ex.getMessage());
    }*/

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ApiResponse> globalHandler(GlobalException exception){
        return new ResponseEntity<>(new ApiResponse(exception.getResponseCode(), exception.getMessage()),
                exception.getResponseCode());
    }


}
