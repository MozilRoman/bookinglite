package com.softserve.edu.bookinglite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ApartmentNotFoundException extends Exception {

    public ApartmentNotFoundException(Long apartmentId) {
        super("Apartment with id = " + apartmentId + " is not exists");
    }
}
