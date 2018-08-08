package com.softserve.edu.bookinglite.service.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

public class CreateBookingDto {

	@NotNull
	private Date checkIn;
	@NotNull
	private Date checkOut;
	@NotNull
	private int numberOfGuests;
	
	public CreateBookingDto() {
		
	}

	public Date getCheckIn() {
		return checkIn;
	}

	public void setCheckIn(Date checkIn) {
		this.checkIn = checkIn;
	}

	public Date getCheckOut() {
		return checkOut;
	}

	public void setCheckOut(Date checkOut) {
		this.checkOut = checkOut;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public void setNumberOfGuests(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}
	
	
}
