package com.softserve.edu.bookinglite.exception.handler;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ExceptionDto> handleAppException(Exception ex, WebRequest request){
        logger.error("Exception: " + ex.getClass().getName() + " Message: " + ex.getMessage() + " Request: " + request.toString());
        HttpStatus status = ex.getClass().getAnnotation(ResponseStatus.class).value();
        ExceptionDto dto = new ExceptionDto();
        dto.setException(ex.getClass().getName());
        dto.setMessage(ex.getMessage());
        return new ResponseEntity<ExceptionDto>(dto,status);
    }
}
