package com.softserve.edu.bookinglite.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.edu.bookinglite.service.PropertyService;
import com.softserve.edu.bookinglite.service.dto.PropertyDto;

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

	@GetMapping("/property/city/{cityName}")
	public List<PropertyDto> getPropertiesByCityName(@PathVariable("cityName") String cityName) {
		return propertyService.getPropertyDtosByCityName(cityName);
	}

	@GetMapping("/property/country/{countryName}")
	public List<PropertyDto> getPropertiesByCountryName(@PathVariable("countryName") String countryName) {
		return propertyService.getPropertyDtosByCountryName(countryName);
	}

	@PostMapping("/property")
	public ResponseEntity<PropertyDto> createProperty(@RequestBody PropertyDto propertyDto, Principal principal) {
		Long userId = Long.parseLong(principal.getName());
		if (propertyService.saveProperty(propertyDto, userId)) {
			return new ResponseEntity<PropertyDto>(HttpStatus.CREATED);
		} else {
			return new ResponseEntity<PropertyDto>(HttpStatus.BAD_REQUEST);

		}
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
