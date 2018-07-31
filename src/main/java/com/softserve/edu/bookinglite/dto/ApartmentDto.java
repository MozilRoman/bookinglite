package com.softserve.edu.bookinglite.dto;

import com.softserve.edu.bookinglite.entity.Amenity;
import com.softserve.edu.bookinglite.entity.ApartmentType;
import com.softserve.edu.bookinglite.entity.Property;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class ApartmentDto {

    //TODO validation
    private Long id;
    private String name;
    private BigDecimal price;
    private int numberOfGuests;
    private ApartmentType apartmentType;
    private Set<Amenity> amenities = new HashSet<>();
//    private Property property;

    public ApartmentDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public ApartmentType getApartmentType() {
        return apartmentType;
    }

    public void setApartmentType(ApartmentType apartmentType) {
        this.apartmentType = apartmentType;
    }

    public Set<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(Set<Amenity> amenities) {
        this.amenities = amenities;
    }

//    public Property getProperty() {
//        return property;
//    }
//
//    public void setProperty(Property property) {
//        this.property = property;
//    }
}
