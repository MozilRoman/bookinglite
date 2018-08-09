package com.softserve.edu.bookinglite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PropertyNotFoundExceprion extends Exception {

	private static final long serialVersionUID = 1L;
	
	public PropertyNotFoundExceprion(Long propertyId) {
		super("Property with id = " + propertyId + " is not exist");
	}
}

