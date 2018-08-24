package com.softserve.edu.bookinglite.exception;

public class NumberOfGuestsException extends Exception{
    public NumberOfGuestsException() {super("The number of guests are not available for this apartment");
    }
}
