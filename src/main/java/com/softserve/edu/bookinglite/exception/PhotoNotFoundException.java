package com.softserve.edu.bookinglite.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PhotoNotFoundException extends Exception {
	public PhotoNotFoundException(String photoName){
		super("Photo with name '"+photoName+"' not found");
	}
}
