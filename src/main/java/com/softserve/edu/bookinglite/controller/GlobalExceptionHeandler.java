package com.softserve.edu.bookinglite.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.softserve.edu.bookinglite.exception.ApartmentNotFoundException;
import com.softserve.edu.bookinglite.exception.PropertyNotFoundExceprion;

@RestControllerAdvice
public class GlobalExceptionHeandler {

	@ExceptionHandler(PropertyNotFoundExceprion.class)
	public ResponseEntity<String> handlePropertyExceptions(PropertyNotFoundExceprion e) {
		HttpStatus httpStatus = null;
		if (e instanceof PropertyNotFoundExceprion) {
			httpStatus = HttpStatus.NOT_FOUND;
		}
		return ResponseEntity.status(httpStatus).body(e.getMessage());
	}

	@ExceptionHandler(ApartmentNotFoundException.class)
	public ResponseEntity<String> handleAparmentException(ApartmentNotFoundException e) {
		HttpStatus httpStatus = null;
		if (e instanceof ApartmentNotFoundException) {
			httpStatus = HttpStatus.NOT_FOUND;
		}
		return ResponseEntity.status(httpStatus).body(e.getMessage());
	}
}
