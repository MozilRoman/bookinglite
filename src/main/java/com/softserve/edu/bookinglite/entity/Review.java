package com.softserve.edu.bookinglite.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class Review {
	
	public Review() {
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="message", nullable = true, length = 1000)
	private String message;
	
	@Column(name="rating", nullable = false)
	private Integer rating;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="booking_id")
	private Booking booking;

	public Long getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	public Integer getRating() {
		return rating;
	}

	public Booking getBooking() {
		return booking;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}
	
}
