package com.softserve.edu.bookinglite.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.edu.bookinglite.entity.Facility;
import com.softserve.edu.bookinglite.entity.Property;
import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.exception.PropertyConfirmOwnerException;
import com.softserve.edu.bookinglite.exception.PropertyNotFoundException;
import com.softserve.edu.bookinglite.repository.PropertyRepository;
import com.softserve.edu.bookinglite.service.dto.CreatePropertyDto;
import com.softserve.edu.bookinglite.service.dto.PropertyDto;
import com.softserve.edu.bookinglite.service.dto.SearchDto;
import com.softserve.edu.bookinglite.service.mapper.PropertyMapper;

@Service
public class PropertyService {

	private final PropertyRepository propertyRepository;

	@Autowired
	public PropertyService(PropertyRepository propertyRepository) {
		this.propertyRepository = propertyRepository;
	}

	@Transactional
	public List<PropertyDto> getAllPropertyDtos() {
		List<PropertyDto> propertyDtos = new ArrayList<>();
		propertyRepository.findAll().forEach(
				p -> propertyDtos.add(PropertyMapper.instance.propertyToBasePropertyDtoWithApartmentAddressUser(p)));
		return propertyDtos;
	}

	@Transactional
	public Optional<Property> getPropertyById(Long id) {
		return propertyRepository.findById(id);
	}

	@Transactional
	public PropertyDto getPropertyDtoById(Long id) throws PropertyNotFoundException {
		Optional<Property> property = getPropertyById(id);
		return property.map(PropertyMapper.instance::propertyToBasePropertyDtoWithApartmentAddressUser)
				.orElseThrow(() -> new PropertyNotFoundException(id));
	}

	@Transactional
	public List<PropertyDto> getPropertyDtosByCityName(String name) {
		List<PropertyDto> propertyDtos = new ArrayList<>();
		propertyRepository.getAllPropertyByCityName(name.toLowerCase()).forEach(
				p -> propertyDtos.add(PropertyMapper.instance.propertyToBasePropertyDtoWithApartmentAddressUser(p)));
		return propertyDtos;
	}

	@Transactional
	public List<PropertyDto> getPropertyDtosByCountryName(String name) {
		List<PropertyDto> propertyDtos = new ArrayList<>();
		propertyRepository.getAllPropertyByCountryName(name.toLowerCase()).forEach(
				p -> propertyDtos.add(PropertyMapper.instance.propertyToBasePropertyDtoWithApartmentAddressUser(p)));
		return propertyDtos;
	}

	@Transactional
	public void save(CreatePropertyDto createPropertyDto, Long userId){
		User user = new User();
		user.setId(userId);
		Property property = PropertyMapper
				.instance.toEntityFromCreatePropertyDto(createPropertyDto);
		property.setUser(user);
		property.setFacilities(getFacilities(createPropertyDto.getFacilityId()));
		propertyRepository.save(property);
		
	}

	@Transactional
	public boolean updateProperty(PropertyDto propertyDto, Long propertyId, Long ownerId)
			throws PropertyNotFoundException, PropertyConfirmOwnerException {
		Property property = propertyRepository.findById(propertyId)
				.orElseThrow(() -> new PropertyNotFoundException(propertyId));
		if (propertyDto != null && property.getUser().getId() == ownerId) {
			property.setName(propertyDto.getName());
			property.setDescription(propertyDto.getDescription());
			property.setPhoneNumber(propertyDto.getPhoneNumber());
			property.setContactEmail(propertyDto.getContactEmail());
			property.setPropertyType(propertyDto.getPropertyType());
			property.setFacilities(propertyDto.getFacilities());
			propertyRepository.save(property);
			return true;
		} else {
			throw new PropertyConfirmOwnerException();
		}
	}

	@Transactional
	public List<PropertyDto> searchProperty(SearchDto searchDto) {
		List<PropertyDto> propertyDtos = new ArrayList<>();
		propertyRepository
				.searchProperties(searchDto.getNumberOfGuests(), searchDto.getCityId(), searchDto.getCountryId(),
						searchDto.getCheckIn(), searchDto.getCheckOut())
				.forEach(p -> propertyDtos
						.add(PropertyMapper.instance.propertyToBasePropertyDtoWithApartmentAddressUser(p)));
		return propertyDtos;
	}

	//ADVANCE SEARCH
	public List<PropertyDto> advanceSearchProperty(AdvanceSearchDto advanceSearchDto) {
		List<PropertyDto> propertyDtos = new ArrayList<>();
		propertyRepository.advanceSearchProperties(
				advanceSearchDto.getNumberOfGuests(),
				advanceSearchDto.getCityId(), 
				advanceSearchDto.getCountryId(),
				advanceSearchDto.getCheckIn(),
				advanceSearchDto.getCheckOut(),
				advanceSearchDto.getAmenitiesId(),
				advanceSearchDto.getFacilitiesId(),
				advanceSearchDto.getPriceFromUser()).forEach(p -> 
				propertyDtos.add(PropertyMapper.instance.propertyToBasePropertyDtoWithApartmentAddressUser(p)));
		return propertyDtos;
	}
	 

	
	@Transactional
	public Page<Property> findPropertyByPage(int page, int size) {
		return propertyRepository.findAll(PageRequest.of(page, size));
	}

	@Transactional
	public List<PropertyDto> getAllPropertyByOwner(Long ownerId) {
		List<PropertyDto> propertyDtos = new ArrayList<>();
		propertyRepository.getAllByUserId(ownerId).forEach(
				p -> propertyDtos.add(PropertyMapper.instance.propertyToBasePropertyDtoWithApartmentAddressUser(p)));
		return propertyDtos;
	}
	
	private Set<Facility> getFacilities(Set<Long> facilitiesId) {
		Set<Facility> facilities = new HashSet<>();
		for (Long id : facilitiesId) {
			Facility facility = new Facility();
			facility.setId(id);
			facilities.add(facility);
		}
		return facilities;
	}
}