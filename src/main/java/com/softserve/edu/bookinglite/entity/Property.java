package com.softserve.edu.bookinglite.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "properties")
public class Property {
	
	public Property() {
	} 

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name" , length = 30, nullable = false)
	private String name;
	@Column(name = "description" , length = 255, nullable = false)
	private String description;
	@Column(name = "rating")
	private Float rating;
	@Column(name = "phone_number" , length = 30, nullable = false)
	private String phoneNumber;
	@Column(name = "contact_email" , length = 50, nullable = false)
	private String contactEmail;

	@ManyToOne(cascade = {
			CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH
			}, fetch = FetchType.LAZY)
	@JoinColumn(name = "property_type_id",nullable = false)
	private PropertyType propertyType;

	@ManyToOne(cascade = { 
			CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH
			}, fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id",nullable = false)
	private Address address;

	@OneToMany(mappedBy = "property", cascade = {
			CascadeType.DETACH,
			CascadeType.MERGE,
			CascadeType.PERSIST,
			CascadeType.REFRESH })
	private List<Apartment> apartments = new ArrayList<>();
	
	// Change CascadeType
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id",nullable = false)
	private User user; 

	@ManyToMany(cascade = {
			CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH
	})
	@JoinTable(name = "property_facilities",
			joinColumns = @JoinColumn(name = "property_id"),
			inverseJoinColumns = @JoinColumn(name = "facility_id"))
	private Set<Facility> facilities = new HashSet<>();
	
	
	@OneToMany(mappedBy = "property" , cascade = CascadeType.ALL)
	private List<Photo> photos = new ArrayList<>();

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

	public PropertyType getPropertyType() {
		return propertyType;
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

	public List<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}

	public List<Apartment> getApartments() {
		return apartments;
	}

	public void setApartments(List<Apartment> apartments) {
		this.apartments = apartments;
	}
}
