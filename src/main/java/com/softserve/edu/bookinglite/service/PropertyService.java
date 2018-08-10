package com.softserve.edu.bookinglite.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.edu.bookinglite.entity.Apartment;
import com.softserve.edu.bookinglite.entity.Booking;
import com.softserve.edu.bookinglite.entity.Property;
import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.exception.PropertyNotFoundExceprion;
import com.softserve.edu.bookinglite.repository.PropertyRepository;
import com.softserve.edu.bookinglite.service.dto.PropertyDto;
import com.softserve.edu.bookinglite.service.dto.SearchDto;
import com.softserve.edu.bookinglite.service.mapper.PropertyMapper;

@Service
public class PropertyService {

	private final PropertyRepository propertyRepository;
	private final UserService userService;

	@Autowired
	public PropertyService(PropertyRepository propertyRepository, UserService userService) {
		this.propertyRepository = propertyRepository;
		this.userService = userService;
		String name = Thread.currentThread().getName();
		System.out.println("++++++++++++" + name + "+++++++++++++" + this.getClass());
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
	public PropertyDto getPropertyDtoById(Long id) throws PropertyNotFoundExceprion {
		Optional<Property> property = getPropertyById(id);
		return property.map(PropertyMapper.instance::propertyToBasePropertyDtoWithApartmentAddressUser)
				.orElseThrow(() -> new PropertyNotFoundExceprion(id));
	}

	@Transactional
	public List<PropertyDto> getPropertyDtosByCityName(String name) {
		List<PropertyDto> propertyDtos = new ArrayList<>();
		for (Property property : propertyRepository.getAllPropertyByCityName(name.toLowerCase())) {
			PropertyDto dto = PropertyMapper.instance.propertyToBasePropertyDtoWithApartmentAddressUser(property);
			propertyDtos.add(dto);
		}
		return propertyDtos;
	}

	@Transactional
	public List<PropertyDto> getPropertyDtosByCountryName(String name) {
		List<PropertyDto> propertyDtos = new ArrayList<>();
		for (Property property : propertyRepository.getAllPropertyByCountryName(name.toLowerCase())) {
			PropertyDto dto = PropertyMapper.instance.propertyToBasePropertyDtoWithApartmentAddressUser(property);
			propertyDtos.add(dto);
		}
		return propertyDtos;
	}
	@Transactional
	public boolean saveProperty(PropertyDto propertyDto, Long userId) {
		Property property = propertyRepository.save(convertToProperty(propertyDto, userId));
		if (property != null) {
			return true;
		} else
			return false;
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

	@Transactional
	public boolean updateProperty(PropertyDto propertyDto, Long propertyId) throws PropertyNotFoundExceprion {
		if (propertyDto != null) {
			Property property = null;
			property = propertyRepository.findById(propertyId)
					.orElseThrow(() -> new PropertyNotFoundExceprion(propertyId));
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

	@Transactional
	public List<PropertyDto> searchProperty(SearchDto searchDto) {
		List<Property> properties = propertyRepository.getAllPropertyByCityId(2L);
		List<PropertyDto> result = new ArrayList<>();
		Boolean conflictboookings = false;
		Integer unbookableApartments = 0;
		for (Property property : properties) {
			for (Apartment apartment : property.getApartments()) {
				if (apartment.getNumberOfGuests() < searchDto.getNumberOfGuests()) {
					unbookableApartments++;
					continue;
				}
				for (Booking booking : apartment.getBookingList()) {
					if ((booking.getCheck_in().after(searchDto.getCheckIn())
							&& booking.getCheck_out().before(searchDto.getCheckIn()))
							|| (booking.getCheck_in().after(searchDto.getCheckOut())
									&& booking.getCheck_out().before(searchDto.getCheckOut()))) {
						conflictboookings = true;
					}
				}
				if (conflictboookings) {
					conflictboookings = false;
					unbookableApartments++;
				}
			}
			if (property.getApartments().size() > unbookableApartments) {
				unbookableApartments = 0;
				result.add(PropertyMapper.instance.propertyToBasePropertyDtoWithAddress(property));
			}
		}
		return result;
	}

	@Transactional
	public Page<Property> fingPropertyByPage(int page, int size) {
		return propertyRepository.findAll(PageRequest.of(page, size));
	}
}