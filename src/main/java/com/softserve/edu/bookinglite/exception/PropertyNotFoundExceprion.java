package com.softserve.edu.bookinglite.exception;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PropertyNotFoundExceprion extends Exception {

	private static final long serialVersionUID = 1L;
	
	public PropertyNotFoundExceprion(Long prodertyId) {
		super("Property with id = " + prodertyId + " is not exist");
	}

	
}
