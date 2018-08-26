package com.softserve.edu.bookinglite.service;

import java.util.*;

import com.softserve.edu.bookinglite.entity.*;
import com.softserve.edu.bookinglite.exception.ApartmentNotFoundException;
import com.softserve.edu.bookinglite.exception.ApartmentUpdateException;
import com.softserve.edu.bookinglite.exception.PropertyNotFoundException;
import com.softserve.edu.bookinglite.repository.*;
import com.softserve.edu.bookinglite.service.dto.CreateApartmentDto;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.softserve.edu.bookinglite.service.dto.ApartmentDto;
import com.softserve.edu.bookinglite.service.mapper.ApartmentMapper;

@Service
public class ApartmentService {

	private final ApartmentRepository apartmentRepository;
	private final PropertyRepository propertyRepository;
	private final ApartmentTypeRepository apartmentTypeRepository;
	private final AmenityRepository amenityRepository;

	@Autowired
	public ApartmentService(ApartmentRepository apartmentRepository, PropertyRepository propertyRepository, ApartmentTypeRepository apartmentTypeRepository, AmenityRepository amenityRepository, UserRepository userRepository) {
		this.apartmentRepository = apartmentRepository;
		this.propertyRepository = propertyRepository;
		this.apartmentTypeRepository = apartmentTypeRepository;
		this.amenityRepository = amenityRepository;
	}

	public ApartmentDto findApartmentDtoById(Long apartmentId) throws ApartmentNotFoundException {
		Optional<Apartment> apartment = apartmentRepository
				.findById(apartmentId);
		return apartment.map(ApartmentMapper.instance::toDto)
				.orElseThrow(()-> new ApartmentNotFoundException(apartmentId));
	}

	public List<ApartmentDto> findAllApartmentsByPropertyId(Long propertyId) throws PropertyNotFoundException {
		Property property = propertyRepository.findById(propertyId)
				.orElseThrow(() -> new PropertyNotFoundException(propertyId));
		List<ApartmentDto> apartmentDtos = new ArrayList<>();
		property.getApartments().forEach(a -> apartmentDtos.add(ApartmentMapper.instance.toDto(a)));
		return apartmentDtos;
	}

	public void saveCreatedApartmentDto (CreateApartmentDto createApartmentDto,
										 Long propertyId, Long userId) throws PropertyNotFoundException {
		Property property = propertyRepository.findById(propertyId)
				.orElseThrow(() -> new PropertyNotFoundException(propertyId));
		if (property.getUser().getId().equals(userId)) {
			Apartment apartment = new Apartment();
			apartment.setName(createApartmentDto.getName());
			apartment.setPrice(createApartmentDto.getPrice());
			apartment.setNumberOfGuests(createApartmentDto.getNumberOfGuests());
			apartment.setProperty(property);
			apartment.setApartmentType(apartmentTypeRepository
					.findById(createApartmentDto.getApartmentTypeId()).get());
			Set<Amenity> amenities = new HashSet<>();
			for (Long id : createApartmentDto.getAmenitiesId()) {
				amenities.add(amenityRepository.findById(id).get());
			}
			apartment.setAmenities(amenities);
			apartmentRepository.save(apartment);
		}
	}

	public boolean updateApartment(ApartmentDto apartmentDto, Long apartmentId, Long userId) throws ApartmentNotFoundException, ApartmentUpdateException {
		Apartment apartment = apartmentRepository.findById(apartmentId)
				.orElseThrow(()-> new ApartmentNotFoundException(apartmentId));
		if (apartment.getProperty().getUser().getId().equals(userId)) {
			apartment.setName(apartmentDto.getName());
			apartment.setPrice(apartmentDto.getPrice());
			apartment.setNumberOfGuests(apartmentDto.getNumberOfGuests());
			apartment.setApartmentType(apartmentDto.getApartmentType());
			apartment.setAmenities(apartmentDto.getAmenities());
			apartmentRepository.save(apartment);
			return true;
		}else {
			throw new ApartmentUpdateException();
		}
	}

	public List<ApartmentType> findApartmentTypes(){
		return apartmentTypeRepository.findAll();
	}

	public List<Amenity> findAmenities(){
		return amenityRepository.findAll();
	}

}