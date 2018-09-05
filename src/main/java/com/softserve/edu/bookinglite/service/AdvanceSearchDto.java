package com.softserve.edu.bookinglite.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

public class AdvanceSearchDto {

    private Long countryId;
    private Long cityId;
    private Date checkIn;
    private Date checkOut;
    @NotNull
    private Integer numberOfGuests;

    private BigDecimal priceFromUser;
    private List<Long> facilitiesId;
    private List<Long> amenitiesId;

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
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

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public BigDecimal getPriceFromUser() {
        return priceFromUser;
    }

    public void setPriceFromUser(BigDecimal priceFromUser) {
        this.priceFromUser = priceFromUser;
    }

    public List<Long> getFacilitiesId() {
        return facilitiesId;
    }

    public void setFacilitiesId(List<Long> facilitiesId) {
        this.facilitiesId = facilitiesId;
    }

    public List<Long> getAmenitiesId() {
        return amenitiesId;
    }

    public void setAmenitiesId(List<Long> amenitiesId) {
        this.amenitiesId = amenitiesId;
    }
}