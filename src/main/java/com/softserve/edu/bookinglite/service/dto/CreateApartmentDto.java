package com.softserve.edu.bookinglite.service.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CreateApartmentDto {

    private Long id;
    @NotBlank
    private String name;
    private BigDecimal price;
    private int numberOfGuests;
    private Long propertyDtoId;
    @NotNull
    private Long apartmentTypeId;
    @NotNull
    private Set<Long> amenitiesId = new HashSet<>();

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

    public Long getPropertyDtoId() {
        return propertyDtoId;
    }

    public void setPropertyDtoId(Long propertyDtoId) {
        this.propertyDtoId = propertyDtoId;
    }

    public Long getApartmentTypeId() {
        return apartmentTypeId;
    }

    public void setApartmentTypeId(Long apartmentTypeId) {
        this.apartmentTypeId = apartmentTypeId;
    }

    public Set<Long> getAmenitiesId() {
        return amenitiesId;
    }

    public void setAmenitiesId(Set<Long> amenitiesId) {
        this.amenitiesId = amenitiesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateApartmentDto that = (CreateApartmentDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
