package com.softserve.edu.bookinglite.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "bookings")
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;//

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="apartment_id",referencedColumnName = "id",nullable = false)
	private Apartment apartment;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName = "id",nullable = false)
	private User user;

	@Column(nullable = false)
    private Date check_in;

	@Column(nullable = false)
    private Date check_out;

	@Column(columnDefinition= "DECIMAL(8,2)",nullable = false)
	@NotNull
    private BigDecimal total_price;

	@ManyToOne
    @JoinColumn(name="status_id", referencedColumnName = "id",nullable = false)
	private BookingStatus bookingstatus;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "review_id")
	private Review review;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Apartment getApartment() {
		return apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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


	public BookingStatus getBookingstatus() {
		return bookingstatus;
	}

	public void setBookingstatus(BookingStatus bookingstatus) {
		this.bookingstatus = bookingstatus;
	}

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}
	
}
