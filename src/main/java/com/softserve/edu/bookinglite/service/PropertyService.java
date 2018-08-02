package com.softserve.edu.bookinglite.service;

import com.softserve.edu.bookinglite.entity.Property;
import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.repository.PropertyRepository;
import com.softserve.edu.bookinglite.service.dto.PropertyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {

	@Autowired private PropertyRepository propertyRepository;
	@Autowired private UserService userService;

	public void saveProperty(PropertyDto propertyDto, Long userId) {
		propertyRepository.save(convertToProperty(propertyDto, userId));
	}

	public Optional<Property> getPropertyById(Long id) {
		return propertyRepository.findById(id);
	}

	public PropertyDto getPropertyDtoById(Long id) {
		return convertToPropertyDto(getPropertyById(id).get());
	}

	public List<Property> getAllProperties() {
		return propertyRepository.findAll();
	}

	private Property convertToProperty(PropertyDto propertyDto, Long userId) {
		Property property = new Property();
		property.setName(propertyDto.getName());
		property.setDescription(propertyDto.getDescription());
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
		propertyDto.setUser(property.getUser());
		propertyDto.setPropertyType(property.getPropertyType());
		propertyDto.setAddress(property.getAddress());
		propertyDto.setFacilities(property.getFacilities());
		return propertyDto;
	}

	public List<PropertyDto> getAllPropertyDtos() {
		List<PropertyDto> propertyDtos = new ArrayList<>();
		for (Property properties : getAllProperties()) {
			PropertyDto dto = convertToPropertyDto(properties);
			propertyDtos.add(dto);
		}
		return propertyDtos;
	}

	public boolean updateProperty(PropertyDto propertyDto, Long propertyId) {
		if (propertyDto != null) {
			Property property = propertyRepository.findById(propertyId).get();
			property.setName(propertyDto.getName());
			property.setDescription(propertyDto.getDescription());
			property.setPhoneNumber(propertyDto.getPhoneNumber());
			property.setContactEmail(propertyDto.getContactEmail());
			property.setPropertyType(propertyDto.getPropertyType());
			property.setFacilities(propertyDto.getFacilities());
			propertyRepository.save(property);
			return true;
		}
		return false;
	}
}
