package com.softserve.edu.bookinglite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softserve.edu.bookinglite.entity.PropertyType;
import com.softserve.edu.bookinglite.repository.PropertyTypeRepository;

@Service
public class PropertyTypeService {

	@Autowired private PropertyTypeRepository propertyTypeRepository;

	public List<PropertyType> getAllPropertyTypies() {
		return propertyTypeRepository.findAll();
	}
	
	public PropertyType getPropertyTypeById(Long id){
		return propertyTypeRepository.getOne(id);
	}

}
