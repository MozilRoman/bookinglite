package com.softserve.edu.bookinglite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongPhotoFormatException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "%s is wrong format for photo";
	public WrongPhotoFormatException(String format) {
		super(String.format(MESSAGE, format));
	}
}
