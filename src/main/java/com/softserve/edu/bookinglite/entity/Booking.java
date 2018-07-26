package com.softserve.edu.bookinglite.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "bookings")
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="apartment_id",referencedColumnName = "id") 
	private Apartment apartment;
	
	
	@NotNull
	@ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName = "id") 
	private User user;
	
	@NotNull
    private Date check_in;
	
	@NotNull
    private Date check_out;
	
	@NotNull
	@Column(columnDefinition= "DECIMAL(8,2)")
    private BigDecimal total_price;
	
	@NotNull
	@ManyToOne(cascade = {
			CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH
			}, fetch = FetchType.LAZY)
    @JoinColumn(name="status_id", referencedColumnName = "id") 
	private BookingStatus bookingstatus;
	
	
	@OneToMany(mappedBy = "booking_id", cascade = {
			CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH
			}, fetch = FetchType.LAZY )
	private  Set<Review> review = new HashSet<>();


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


	public Set<Review> getReview() {
		return review;
	}


	public void setReview(Set<Review> review) {
		this.review = review;
	} 
	
	
}
