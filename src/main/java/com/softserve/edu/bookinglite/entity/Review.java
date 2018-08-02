package com.softserve.edu.bookinglite.entity;

import javax.persistence.*;

@Entity
@Table(name = "reviews")
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="message", nullable = false)
	private String message;

	@Column(name="rating", nullable = false)
	private Integer rating;

	@OneToOne(mappedBy = "review",fetch = FetchType.LAZY)
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

	public void setId(Long id) {
		this.id = id;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

}
