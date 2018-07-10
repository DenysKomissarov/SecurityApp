package com.security.app.exception;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler{

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = " User not found. ")
    @ExceptionHandler(ServiceException.class)
    public void handleUnauthorized(){ }



}
