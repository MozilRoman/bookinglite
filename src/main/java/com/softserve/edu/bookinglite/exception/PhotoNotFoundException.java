package com.softserve.edu.bookinglite.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PhotoNotFoundException extends Exception {
	private static final String MESSAGE = "Photo with name %s not found";
	public PhotoNotFoundException(String photoName){
		super(String.format(MESSAGE, photoName));
	}
}
