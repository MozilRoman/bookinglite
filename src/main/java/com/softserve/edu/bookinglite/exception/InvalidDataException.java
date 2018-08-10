package com.softserve.edu.bookinglite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDataException  extends Exception{

	private static final long serialVersionUID = 1L;
	
	public InvalidDataException() {
		super("You entered invalid data. Check field date and number of guests ");
	}
}
