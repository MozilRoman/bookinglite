package com.softserve.edu.bookinglite.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="photos")
public class Photo {
	
	public Photo() {
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@Column(name="url", nullable = false, length = 100)
	private String url;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="property_id")
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