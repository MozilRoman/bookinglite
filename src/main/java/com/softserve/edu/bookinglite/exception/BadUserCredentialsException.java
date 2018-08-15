package com.softserve.edu.bookinglite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadUserCredentialsException extends Exception {

    public BadUserCredentialsException(){
        super("Bad Credentials");
    }
}
