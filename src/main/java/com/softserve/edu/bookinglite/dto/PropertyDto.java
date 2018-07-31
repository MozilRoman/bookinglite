package com.softserve.edu.bookinglite.dto;

import java.util.Set;

import com.softserve.edu.bookinglite.entity.Address;
import com.softserve.edu.bookinglite.entity.Facility;
import com.softserve.edu.bookinglite.entity.PropertyType;
import com.softserve.edu.bookinglite.entity.User;

public class PropertyDto {
	
	private Long id;
	
	private String name;
	
	private String description;
	
	private Float rating;
	
	private String phoneNumber;

	private String contactEmail;
	
	private User user;

	//	// NOT NULL
	private PropertyType propertyType;
//	// NOT NULL
	private Address address;

	
//	private List<Apartment> apartments;
//	
	private Set<Facility> facilities;
//	
//	private List<Photo> photos;

	
	
	public PropertyDto() {
	}

	public PropertyDto(String name, String description, Float rating, String phoneNumber,
			String contactEmail) {
		super();
		this.name = name;
		this.description = description;
		this.rating = rating;
		this.phoneNumber = phoneNumber;
		this.contactEmail = contactEmail;
	}

	
	
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public PropertyType getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(PropertyType propertyType) {
		this.propertyType = propertyType;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Float getRating() {
		return rating;
	}

	public void setRating(Float rating) {
		this.rating = rating;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<Facility> getFacilities() {
		return facilities;
	}

	public void setFacilities(Set<Facility> facilities) {
		this.facilities = facilities;
	}
	
	


	
}
