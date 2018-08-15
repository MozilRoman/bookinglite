package com.softserve.edu.bookinglite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserNotFoundException extends Exception {
    public UserNotFoundException(String email){
        super("User with email'"+ email +"' not found");
    }
    public UserNotFoundException() {
        super("User with this id not found");
    }
}
