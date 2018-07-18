//package com.security.app.controller.advice;
//
//import com.security.app.responce.ApiResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//@ControllerAdvice
//public class EntityExceptionHandler{
//
//    private static final Logger logger = LoggerFactory.getLogger(EntityExceptionHandler.class);
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ApiResponse dataNotFound(RuntimeException ex){
//        logger.info(ex.getMessage());
//        String bodyOfResponse = ex.getMessage().equals("") ? "wrong data!" : ex.getMessage();
//        return new ApiResponse(HttpStatus.BAD_REQUEST, bodyOfResponse);
//    }
//
//    @ExceptionHandler(GlobalException.class)
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    protected ApiResponse handleAccessDenied(RuntimeException ex) {
//        logger.info(ex.getMessage());
//        String bodyOfResponse = ex.getMessage().equals("") ? "You don't have permission!" : ex.getMessage();
//        return new ApiResponse(HttpStatus.FORBIDDEN, bodyOfResponse);
//    }
//}
