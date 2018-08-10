package com.softserve.edu.bookinglite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookingOwnerNotFoundException  extends Exception{

	private static final long serialVersionUID = 1L;
	
	public BookingOwnerNotFoundException() {
		super("At this moment you don`t have some bookings in your property");
	}
}
