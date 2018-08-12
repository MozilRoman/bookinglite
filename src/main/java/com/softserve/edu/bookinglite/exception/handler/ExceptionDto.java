package com.softserve.edu.bookinglite.exception.handler;

public class ExceptionDto {
    private String exception;
    private String message;

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
