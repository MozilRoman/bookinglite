package com.softserve.edu.bookinglite.service.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.softserve.edu.bookinglite.entity.BookingStatus;

public class BookingDto {

	
	private Long bookingId; 
	@NotNull
	private ApartmentDto apartmentDto;
	@NotNull
	private UserDto userDto;
	@NotNull
	private Date checkIn;
	@NotNull
	private Date checkOut;
	@NotNull
	private BigDecimal totalPrice;
	@NotNull
	private BookingStatus bookingStatus;
	
	private ReviewDto reviewDto;
	
	public BookingDto() {
		
	}

	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
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

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public BookingStatus getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(BookingStatus bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public ReviewDto getReviewDto() {
		return reviewDto;
	}

	public void setReviewDto(ReviewDto reviewDto) {
		this.reviewDto = reviewDto;
	}

	
}
