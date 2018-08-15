package com.softserve.edu.bookinglite.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserIsNotVerifiedException extends Exception {
    public UserIsNotVerifiedException(){
        super("User with this email is not verified. Check your email");
    }
}
