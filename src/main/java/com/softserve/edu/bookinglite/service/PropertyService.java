package com.softserve.edu.bookinglite.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softserve.edu.bookinglite.dto.PropertyDto;
import com.softserve.edu.bookinglite.entity.Property;
import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.repository.PropertyRepository;

@Service
public class PropertyService {

	@Autowired private PropertyRepository propertyRepository;
	@Autowired private UserService userService;

	public void saveProperty(PropertyDto propertyDto, Long userId) {
		propertyRepository.save(convertToProperty(propertyDto, userId));
	}

	public void updateProperty(Property property) {
		propertyRepository.save(property);
	}

	public Property getPropertyById(Long id) {
		return propertyRepository.getOne(id);
	}
	
	public PropertyDto getPropertyDtoById(Long id) {
		return convertToPropertyDto(getPropertyById(id));
	}

	public List<Property> getAllProperties() {
		return propertyRepository.findAll();
	}

	private Property convertToProperty(PropertyDto propertyDto, Long userId) {
		Property property = new Property();
		property.setName(propertyDto.getName());
		property.setDescription(propertyDto.getDescription());
		property.setRating(propertyDto.getRating());
		property.setPhoneNumber(propertyDto.getPhoneNumber());
		property.setContactEmail(propertyDto.getContactEmail());
		Optional<User> user = userService.getUserById(userId);
		property.setUser(user.get());
		property.setPropertyType(propertyDto.getPropertyType());
		property.setAddress(propertyDto.getAddress());
		property.setFacilities(propertyDto.getFacilities());
		return property;
	}

	private PropertyDto convertToPropertyDto(Property property) {
		PropertyDto propertyDto = new PropertyDto();
		propertyDto.setId(property.getId());
		propertyDto.setName(property.getName());
		propertyDto.setDescription(property.getDescription());
		propertyDto.setRating(property.getRating());
		propertyDto.setPhoneNumber(property.getPhoneNumber());
		propertyDto.setContactEmail(property.getContactEmail());
//		propertyDto.setUser(property.getUser());
//		propertyDto.setPropertyType(property.getPropertyType());
//		propertyDto.setAddress(property.getAddress());
//		propertyDto.setFacilities(property.getFacilities());
		return propertyDto;
	}
	
	public List<PropertyDto> getAllPropertyDtos(){
		List<PropertyDto> propertyDtos = new ArrayList<>();
		for(Property properties : getAllProperties()) {
			PropertyDto dto = convertToPropertyDto(properties);
			propertyDtos.add(dto);
		}
		return propertyDtos;
	}
}
