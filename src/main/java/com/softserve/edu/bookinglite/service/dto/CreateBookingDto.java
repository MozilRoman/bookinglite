package com.softserve.edu.bookinglite.service.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

public class CreateBookingDto {

	@NotNull
	private Date check_in;
	@NotNull
	private Date check_out;
	@NotNull
	private int numberOfGuests;
	
	public CreateBookingDto() {
		
	}

	public Date getCheck_in() {
		return check_in;
	}

	public void setCheck_in(Date check_in) {
		this.check_in = check_in;
	}

	public Date getCheck_out() {
		return check_out;
	}

	public void setCheck_out(Date check_out) {
		this.check_out = check_out;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public void setNumberOfGuests(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}
	
	
}
