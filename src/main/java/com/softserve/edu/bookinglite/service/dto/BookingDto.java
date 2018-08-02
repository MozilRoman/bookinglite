package com.softserve.edu.bookinglite.service.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.softserve.edu.bookinglite.entity.Apartment;
import com.softserve.edu.bookinglite.entity.BookingStatus;
import com.softserve.edu.bookinglite.entity.Review;
import com.softserve.edu.bookinglite.entity.User;

public class BookingDto {

    private Long id;
    @NotNull
    private Apartment apartment;
    @NotNull
    private User user;
    @NotNull
    private Date check_in;
    @NotNull
    private Date check_out;
    @NotNull
    private BigDecimal total_price;
    @NotNull
    private BookingStatus bookingstatus;

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