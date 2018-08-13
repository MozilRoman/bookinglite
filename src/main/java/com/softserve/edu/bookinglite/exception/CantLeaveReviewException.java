package com.softserve.edu.bookinglite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CantLeaveReviewException extends Exception {

    public CantLeaveReviewException() {
        super("You can't leave feedback before check out date.");
    }
}
