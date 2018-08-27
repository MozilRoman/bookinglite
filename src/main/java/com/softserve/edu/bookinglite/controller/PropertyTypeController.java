package com.softserve.edu.bookinglite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.edu.bookinglite.entity.PropertyType;
import com.softserve.edu.bookinglite.service.PropertyTypeService;

@RestController
@RequestMapping("/api")
public class PropertyTypeController {

	private final PropertyTypeService propertyTypeService;
	
	@Autowired
	public PropertyTypeController(PropertyTypeService propertyTypeService) {
		this.propertyTypeService = propertyTypeService;
	}
	
	@GetMapping("/propertytype")
	public List<PropertyType> getAllPropertyTypes(){
		return propertyTypeService.getAllPropertyTypes();
	}
}
