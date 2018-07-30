package com.softserve.edu.bookinglite.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "property_types")
public class PropertyType {
	
	public PropertyType() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 50, nullable = false)
	private String name;

	@OneToMany(mappedBy = "propertyType", cascade = { 
			CascadeType.DETACH, 
			CascadeType.MERGE, 
			CascadeType.PERSIST,
			CascadeType.REFRESH })
	private List<Property> properties = new ArrayList<>();

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

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
}
