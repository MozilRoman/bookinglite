package com.softserve.edu.bookinglite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookingCancelException  extends Exception{

	private static final long serialVersionUID = 1L;
	
	public BookingCancelException(String bookingtStatus) {
		super("You can not change status " + bookingtStatus + " on Cancel");
	}
}
