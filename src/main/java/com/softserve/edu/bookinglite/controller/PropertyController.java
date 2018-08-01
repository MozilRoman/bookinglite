 package com.softserve.edu.bookinglite.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.edu.bookinglite.dto.PropertyDto;
import com.softserve.edu.bookinglite.service.PropertyService;

@RestController
@RequestMapping("/api")
public class PropertyController {

	@Autowired private PropertyService propertyService;

	@GetMapping("/property")
	public List<PropertyDto> getAllProperties(){
		return propertyService.getAllPropertyDtos();
	}
	
	@GetMapping("/property/{propertyId}")
	public PropertyDto getPropertyById(@PathVariable("propertyId") Long id) {
		return propertyService.getPropertyDtoById(id);
	}

	@PostMapping("/property")
	public ResponseEntity<Void> createProperty(@RequestBody PropertyDto propertyDto, 
			Principal principal) {
		Long userId = Long.parseLong(principal.getName());
		propertyService.saveProperty(propertyDto,userId);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
}
