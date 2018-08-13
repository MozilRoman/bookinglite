package com.softserve.edu.bookinglite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ReviewOwnerException extends Exception {

    public ReviewOwnerException() {
        super("You are not owner of this booking.");
    }
}
