package com.softserve.edu.bookinglite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NumberOfGuestsException extends Exception {

    private static final long serialVersionUID = 1L;

    public NumberOfGuestsException() {
        super("The number of guests are not available for this apartment");
    }
}
