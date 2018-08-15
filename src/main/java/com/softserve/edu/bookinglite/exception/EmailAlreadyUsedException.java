package com.softserve.edu.bookinglite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailAlreadyUsedException extends Exception {

    public EmailAlreadyUsedException(String email){
        super("This email '"+ email + "' already used");
    }

}
