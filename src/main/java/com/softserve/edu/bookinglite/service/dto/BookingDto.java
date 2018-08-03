package com.softserve.edu.bookinglite.service.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.softserve.edu.bookinglite.entity.Apartment;
import com.softserve.edu.bookinglite.entity.BookingStatus;
import com.softserve.edu.bookinglite.entity.Review;
import com.softserve.edu.bookinglite.entity.User;

public class BookingDto {

	
	private Long booking_id;

	private ApartmentDto apartmentDto;

	private UserDto userDto;
	@NotNull
	private Date check_in;
	@NotNull
	private Date check_out;
	@NotNull
	private BigDecimal total_price;
	@NotNull
	private String bookingstatus;
	
	private String review;
	
	public BookingDto() {
		
	}
	
	public Long getId() {
		return booking_id;
	}

	public void setId(Long id) {
		this.booking_id = id;
	}

	public ApartmentDto getApartmentDto() {
		return apartmentDto;
	}

	public void setApartmentDto(ApartmentDto apartmentDto) {
		this.apartmentDto = apartmentDto;
	}

	public UserDto getUserDto() {
		return userDto;
	}

	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
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

	public BigDecimal getTotal_price() {
		return total_price;
	}

	public void setTotal_price(BigDecimal total_price) {
		this.total_price = total_price;
	}

	public Long getBooking_id() {
		return booking_id;
	}

	public void setBooking_id(Long booking_id) {
		this.booking_id = booking_id;
	}

	public String getBookingstatus() {
		return bookingstatus;
	}

	public void setBookingstatus(String bookingstatus) {
		this.bookingstatus = bookingstatus;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}
}
