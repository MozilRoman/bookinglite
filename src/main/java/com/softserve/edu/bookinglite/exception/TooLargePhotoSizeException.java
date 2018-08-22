package com.softserve.edu.bookinglite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TooLargePhotoSizeException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "Available size: 15Mb. Your file have %d bytes";
	public TooLargePhotoSizeException(long byteSize) {
		super(String.format(MESSAGE,byteSize));
	}
}
