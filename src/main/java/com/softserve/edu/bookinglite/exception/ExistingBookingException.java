package com.softserve.edu.bookinglite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExistingBookingException  extends Exception{

	private static final long serialVersionUID = 1L;
	
	public ExistingBookingException() {
		super("This booking already exist. Enter other dates");
	}
}
