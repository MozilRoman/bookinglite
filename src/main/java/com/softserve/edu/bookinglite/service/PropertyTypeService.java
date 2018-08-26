package com.softserve.edu.bookinglite.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softserve.edu.bookinglite.entity.PropertyType;
import com.softserve.edu.bookinglite.repository.PropertyTypeRepository;

@Service
public class PropertyTypeService {

	private final PropertyTypeRepository propertyTypeRepository;

	@Autowired
	public PropertyTypeService(PropertyTypeRepository propertyTypeRepository) {
		this.propertyTypeRepository = propertyTypeRepository;
	} 
	@Transactional
	public List<PropertyType> getAllPropertyTypes(){
		return propertyTypeRepository.findAll();
	}
	
	@Transactional
	public PropertyType getPropertyTypeById(Long propertyTypeId) {
		return propertyTypeRepository.findById(propertyTypeId).get();
	}
}
