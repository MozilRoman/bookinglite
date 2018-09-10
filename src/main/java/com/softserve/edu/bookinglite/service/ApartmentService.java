package com.softserve.edu.bookinglite.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.edu.bookinglite.entity.Amenity;
import com.softserve.edu.bookinglite.entity.Apartment;
import com.softserve.edu.bookinglite.entity.ApartmentType;
import com.softserve.edu.bookinglite.entity.Property;
import com.softserve.edu.bookinglite.exception.ApartmentNotFoundException;
import com.softserve.edu.bookinglite.exception.ApartmentUpdateException;
import com.softserve.edu.bookinglite.exception.PropertyNotFoundException;
import com.softserve.edu.bookinglite.exception.UserNotFoundException;
import com.softserve.edu.bookinglite.repository.AmenityRepository;
import com.softserve.edu.bookinglite.repository.ApartmentRepository;
import com.softserve.edu.bookinglite.repository.ApartmentTypeRepository;
import com.softserve.edu.bookinglite.repository.PropertyRepository;
import com.softserve.edu.bookinglite.service.dto.ApartmentDto;
import com.softserve.edu.bookinglite.service.dto.CreateApartmentDto;
import com.softserve.edu.bookinglite.service.mapper.ApartmentMapper;

@Service
public class ApartmentService {

	private final ApartmentRepository apartmentRepository;
	private final PropertyRepository propertyRepository;
	private final ApartmentTypeRepository apartmentTypeRepository;
	private final AmenityRepository amenityRepository;

	
	@Autowired
	public ApartmentService(ApartmentRepository apartmentRepository, PropertyRepository propertyRepository,
			ApartmentTypeRepository apartmentTypeRepository, AmenityRepository amenityRepository) {
		this.apartmentRepository = apartmentRepository;
		this.propertyRepository = propertyRepository;
		this.apartmentTypeRepository = apartmentTypeRepository;
		this.amenityRepository = amenityRepository;
	}
	
	@Transactional
	public ApartmentDto findApartmentDtoById(Long apartmentId) throws ApartmentNotFoundException {
		Apartment apartment = apartmentRepository.findById(apartmentId)
				.orElseThrow(() -> new ApartmentNotFoundException(apartmentId));
		return ApartmentMapper.instance.toDto(apartment);
	}
	
	@Transactional
	public List<ApartmentDto> findAllApartmentsByPropertyId(Long propertyId) throws PropertyNotFoundException {
		Property property = propertyRepository.findById(propertyId)
				.orElseThrow(() -> new PropertyNotFoundException(propertyId));
		List<ApartmentDto> apartmentDtos = new ArrayList<>();
		property.getApartments().forEach(a -> apartmentDtos.add(ApartmentMapper.instance.toDto(a)));
		return apartmentDtos;
	}
	
	@Transactional
	public boolean save(CreateApartmentDto createApartmentDto, Long propertyId, Long userId)
			throws PropertyNotFoundException {

		Property property = propertyRepository.findById(propertyId)
				.orElseThrow(() -> new PropertyNotFoundException(propertyId));
		if (property.getUser().getId().equals(userId)) {
			Apartment apartment = ApartmentMapper.instance.toEntity(createApartmentDto);
			apartment.setProperty(property);
			apartment.setAmenities(getAmenities(createApartmentDto.getAmenitiesId()));
			apartment.setNumberOfGuests(createApartmentDto.getNumberOfGuests());
			apartmentRepository.save(apartment);
			return true;
		} else {
			new UserNotFoundException(property.getUser().getEmail());
			return false;
		}
	}
	
	@Transactional
	public boolean updateApartment(CreateApartmentDto createApartmentDto, Long apartmentId, Long userId)
			throws ApartmentNotFoundException, ApartmentUpdateException {
		Apartment apartment = apartmentRepository.findById(apartmentId)
				.orElseThrow(() -> new ApartmentNotFoundException(apartmentId));
		if (apartment.getProperty().getUser().getId().equals(userId)) {
			apartment.setName(createApartmentDto.getName());
			apartment.setPrice(createApartmentDto.getPrice());
			apartment.setNumberOfGuests(createApartmentDto.getNumberOfGuests());
			ApartmentType apartmentType = new ApartmentType();
			apartmentType.setId(createApartmentDto.getApartmentTypeId());
			apartment.setApartmentType(apartmentType);
			apartment.setAmenities(getAmenities(createApartmentDto.getAmenitiesId()));
			apartmentRepository.save(apartment);
			return true;
		} else {
			throw new ApartmentUpdateException();
		}
	}
	
	@Transactional
	public List<ApartmentType> findApartmentTypes() {
		return apartmentTypeRepository.findAll();
	}
	
	@Transactional
	public List<Amenity> findAmenities() {
		return amenityRepository.findAll();
	}

	private Set<Amenity> getAmenities(Set<Long> ids) {
		Set<Amenity> amenities = new HashSet<>();
		for (Long id : ids) {
			amenities.add(amenityRepository.findById(id).get());
		}
		return amenities;
	}
}