package com.softserve.edu.bookinglite.entity;

import javax.persistence.*;

@Entity
@Table(name="photos")
public class Photo {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@Column(unique = true, name="url", nullable = false)
	private String url;
	
	@ManyToOne
	@JoinColumn(name="property_id",nullable = false)
	private Property property;

	public Long getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public Property getProperty() {
		return property;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setProperty(Property property) {
		this.property = property;  
	}
	
	
	
}
