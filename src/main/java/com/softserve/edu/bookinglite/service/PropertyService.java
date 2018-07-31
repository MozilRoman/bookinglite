package com.softserve.edu.bookinglite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softserve.edu.bookinglite.entity.Property;
import com.softserve.edu.bookinglite.repository.PropertyRepository;

@Service
public class PropertyService {

	@Autowired private PropertyRepository propertyRepository;
	
	public void saveProperty(Property property) {
		propertyRepository.save(property);
	}
	
	public void updateProperty(Property property) {
		propertyRepository.save(property);
	}
	
	public Property getPropertyById(Long id) {
		return propertyRepository.getOne(id);
	}
	
	public List<Property> getAllProperties(){
		return propertyRepository.findAll();
	}
	
	
}
