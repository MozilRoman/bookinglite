package com.softserve.edu.bookinglite.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.edu.bookinglite.dto.PropertyDto;
import com.softserve.edu.bookinglite.entity.Address;
import com.softserve.edu.bookinglite.entity.Facility;
import com.softserve.edu.bookinglite.entity.Property;
import com.softserve.edu.bookinglite.entity.PropertyType;
import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.service.AddressService;
import com.softserve.edu.bookinglite.service.PropertyService;
import com.softserve.edu.bookinglite.service.PropertyTypeService;
import com.softserve.edu.bookinglite.service.UserService;


@RestController
@RequestMapping("/api")
public class PropertyController {

	@Autowired
	private PropertyService propertyService;
	@Autowired
	private PropertyTypeService propertyTypeService;
	@Autowired
	private UserService userService;
	// @Autowired private FacilityService facilityService;
	@Autowired
	private AddressService addressService;

	//////////////////////////////
	
	
	///////////////////////////
	
	

	@GetMapping("/property")
	public List<PropertyDto> getListProperties() {
		List<PropertyDto> propertyDtos = new ArrayList<>();
		for (Property properties : propertyService.getAllProperties()) {
			PropertyDto pd = new PropertyDto();
			pd.setName(properties.getName());
			pd.setDescription(properties.getDescription());
			pd.setPhoneNumber(properties.getPhoneNumber());
			// pd.setAddress(properties.getAddress());
			pd.setContactEmail(properties.getContactEmail());
			// pd.setPropertyType(properties.getPropertyType());
			pd.setRating(properties.getRating());
			// pd.setUser(properties.getUser());
			propertyDtos.add(pd);
		}
		return propertyDtos;
	}

	@PostMapping("/property")
	public ResponseEntity<Void> createProperty(@RequestBody PropertyDto propertyDto, 
			Principal principal) {
		Property property = new Property();
		property.setName(propertyDto.getName());
		property.setDescription(propertyDto.getDescription());
		property.setRating(propertyDto.getRating());
		property.setPhoneNumber(propertyDto.getPhoneNumber());
		property.setContactEmail(propertyDto.getContactEmail());

		Optional<User> user = userService.getUserById(Long.parseLong(principal.getName()));
//		User user = userService.getUserById(Long.parseLong(principal.getName()));
		property.setUser(user.get());
		
		PropertyType propertyType = propertyDto.getPropertyType();
		property.setPropertyType(propertyType);
		
		Address address = propertyDto.getAddress();
		property.setAddress(address);
		
		Set<Facility> facilities = propertyDto.getFacilities();
		property.setFacilities(facilities);
		
		
		propertyService.saveProperty(property);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

}
