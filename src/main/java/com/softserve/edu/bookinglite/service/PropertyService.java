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

import com.softserve.edu.bookinglite.entity.Address;
import com.softserve.edu.bookinglite.entity.Apartment;
import com.softserve.edu.bookinglite.entity.Booking;
import com.softserve.edu.bookinglite.entity.City;
import com.softserve.edu.bookinglite.entity.Country;
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
	
	@Autowired
	private PropertyTypeService propertyTypeService;

	@Autowired
	private FacilityService facilityService;

	@Autowired
	private CountryService countryService;
	
	@Autowired
	private CityService cityService;
	
	
	private final PropertyRepository propertyRepository;
	private final UserService userService;

	@Autowired
	public PropertyService(PropertyRepository propertyRepository, UserService userService) {
		this.propertyRepository = propertyRepository;
		this.userService = userService;
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

	@SuppressWarnings("unused")
	@Transactional
	public boolean saveProperty(PropertyDto propertyDto, Long userId) {
		Optional<User> user = userService.getUserById(userId);
		Property property = PropertyMapper.instance.toEntity(propertyDto);
		property.setUser(user.get());
		propertyRepository.save(property);
		if (property != null) {
			return true;
		}
		return false;
	}
	
	@Transactional
	public void saveCreatePropertyDto(CreatePropertyDto  createPropertyDto,
			Long userId) {
		
	Property property = new Property();
	property.setName(createPropertyDto.getName());
	System.out.println(createPropertyDto.getName());
	property.setDescription(createPropertyDto.getDescription());
	property.setPhoneNumber(createPropertyDto.getPhoneNumber());
	property.setContactEmail(createPropertyDto.getContactEmail());
	property.setUser(userService.getUserById(userId).get());
	property.setPropertyType(propertyTypeService
			.getPropertyTypeById(createPropertyDto.getPropertyTypeId()));
	
	System.out.println("propertyTYpe" + createPropertyDto.getPropertyTypeId());
	System.out.println("countryID " +createPropertyDto.getCountryId());
	
	Set<Facility> facilities = new HashSet<>();
		for(Long id : createPropertyDto.getFacilityId()) {
			facilities.add(facilityService.getFacilityById(id));
		}
		property.setFacilities(facilities);

	Country country = countryService.getCountryByid(createPropertyDto.getCountryId());
	City city = cityService.getCityByid(createPropertyDto.getCityId());
	city.setCountry(country);	
	Address address = new Address();
	address.setAddressLine(createPropertyDto.getAddressLine());
	address.setZip(createPropertyDto.getZip());
	address.setCity(cityService.getCityByid(createPropertyDto.getCityId()));
	
	property.setAddress(address);

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
					if ((booking.getCheckIn().after(searchDto.getCheckIn())
							&& booking.getCheckOut().before(searchDto.getCheckIn()))
							|| (booking.getCheckIn().after(searchDto.getCheckOut())
									&& booking.getCheckOut().before(searchDto.getCheckOut()))) {
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
	public Page<Property> findPropertyByPage(int page, int size) {
		return propertyRepository.findAll(PageRequest.of(page, size));
	}
}