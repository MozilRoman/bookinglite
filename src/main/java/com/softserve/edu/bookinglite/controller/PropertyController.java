package com.softserve.edu.bookinglite.controller;

import com.softserve.edu.bookinglite.service.PropertyService;
import com.softserve.edu.bookinglite.service.dto.PropertyDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PropertyController {

	private final PropertyService propertyService;

	@Autowired
	public PropertyController(PropertyService propertyService) {
		this.propertyService = propertyService;
	}

	@GetMapping("/property")
	public List<PropertyDto> getAllProperties() {
		return propertyService.getAllPropertyDtos();
	}

	@GetMapping("/property/{propertyId}")
	public PropertyDto getPropertyById(@PathVariable("propertyId") Long id) {
		return propertyService.getPropertyDtoById(id);
	}

	@PostMapping("/property")
	public ResponseEntity<PropertyDto> createProperty(@RequestBody PropertyDto propertyDto, Principal principal) {
		Long userId = Long.parseLong(principal.getName());
		propertyService.saveProperty(propertyDto, userId);
		return new ResponseEntity<PropertyDto>(HttpStatus.CREATED);
	}

	@PutMapping("/property/{propertyId}")
	public ResponseEntity<PropertyDto> update(@RequestBody PropertyDto propertyDto,
			@PathVariable("propertyId") Long id) {
		if (propertyService.updateProperty(propertyDto, id)) {
			return new ResponseEntity<PropertyDto>(HttpStatus.OK);
		} else {
			return new ResponseEntity<PropertyDto>(HttpStatus.BAD_REQUEST);
		}
	}
}
