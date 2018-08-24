package com.softserve.edu.bookinglite.service.dto;

import com.softserve.edu.bookinglite.entity.Amenity;
import com.softserve.edu.bookinglite.entity.ApartmentType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class ApartmentDto {

    private Long id;
    @NotBlank
    private String name;
    private BigDecimal price;
    private int numberOfGuests;
    @NotNull
    private ApartmentType apartmentType;
    @NotNull
    private Set<Amenity> amenities = new HashSet<>();

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amenities == null) ? 0 : amenities.hashCode());
		result = prime * result + ((apartmentType == null) ? 0 : apartmentType.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + numberOfGuests;
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApartmentDto other = (ApartmentDto) obj;
		if (amenities == null) {
			if (other.amenities != null)
				return false;
		} else if (!amenities.equals(other.amenities))
			return false;
		if (apartmentType == null) {
			if (other.apartmentType != null)
				return false;
		} else if (!apartmentType.equals(other.apartmentType))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (numberOfGuests != other.numberOfGuests)
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		return true;
	}

}
