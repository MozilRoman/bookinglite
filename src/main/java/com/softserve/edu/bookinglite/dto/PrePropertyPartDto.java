package com.softserve.edu.bookinglite.dto;

import com.softserve.edu.bookinglite.entity.Address;
import com.softserve.edu.bookinglite.entity.Facility;
import com.softserve.edu.bookinglite.entity.PropertyType;

public class PrePropertyPartDto {

	private PropertyType propertyType;
	
	private Address address;
	
	private Facility facility;

	public PropertyType getPropertyType() {
		return propertyType;
	}
	
	public PrePropertyPartDto() {
		super();
	}

	public PrePropertyPartDto(PropertyType propertyType, Address address, Facility facility) {
		this.propertyType = propertyType;
		this.address = address;
		this.facility = facility;
	}

	public void setPropertyType(PropertyType propertyType) {
		this.propertyType = propertyType;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Facility getFacility() {
		return facility;
	}

	public void setFacility(Facility facility) {
		this.facility = facility;
	}
	
	
}
