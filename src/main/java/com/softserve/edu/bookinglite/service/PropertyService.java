package com.softserve.edu.bookinglite.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softserve.edu.bookinglite.entity.Property;
import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.repository.PropertyRepository;
import com.softserve.edu.bookinglite.service.dto.PropertyDto;
import com.softserve.edu.bookinglite.service.mapper.PropertyMapper;
import com.softserve.edu.bookinglite.service.mapper.UserMapper;

import org.springframework.transaction.annotation.Transactional;
@Service
public class PropertyService {

	private PropertyRepository propertyRepository;
	private UserService userService;

	@Autowired
	public PropertyService(PropertyRepository propertyRepository, UserService userService) {
		this.propertyRepository = propertyRepository;
		this.userService = userService;
	}
    
    @Transactional
	public List<PropertyDto> getAllPropertyDtos() {
		List<PropertyDto> propertyDtos = new ArrayList<>();
		for (Property property : propertyRepository.findAll()) {
			PropertyDto dto = PropertyMapper.instance.propertyToBasePropertyDtoWithApartmentAddressUser(property);
			propertyDtos.add(dto);
		}
		return propertyDtos;
	}
    @Transactional
	public Optional<Property> getPropertyById(Long id) {
		return propertyRepository.findById(id);
	}
    @Transactional
	public PropertyDto getPropertyDtoById(Long id) {
		Optional<Property> property = getPropertyById(id);
		
		return property.map(PropertyMapper
				.instance::propertyToBasePropertyDtoWithApartmentAddressUser).orElse(null);
	}
	@Transactional
	public void saveProperty(PropertyDto propertyDto, Long userId) {
		propertyRepository.save(convertToProperty(propertyDto, userId));
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

//	private PropertyDto convertToPropertyDto(Property property) {
//		PropertyDto propertyDto = new PropertyDto();
//		propertyDto.setId(property.getId());
//		propertyDto.setName(property.getName());
//		propertyDto.setDescription(property.getDescription());
//		propertyDto.setRating(property.getRating());
//		propertyDto.setPhoneNumber(property.getPhoneNumber());
//		propertyDto.setContactEmail(property.getContactEmail());
//		// propertyDto.setUser(property.getUser());
//		propertyDto.setUserDto(UserMapper.instance.UserToBaseUserDtoWithRolesAndAddress(property.getUser()));
//		propertyDto.setPropertyType(property.getPropertyType());
//		propertyDto.setAddress(property.getAddress());
//		propertyDto.setFacilities(property.getFacilities());
//		return propertyDto;
//	}


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
