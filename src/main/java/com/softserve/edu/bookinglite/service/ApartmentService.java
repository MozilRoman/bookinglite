package com.softserve.edu.bookinglite.service;

import java.util.ArrayList;
import java.util.List;

import com.softserve.edu.bookinglite.exception.ApartmentNotFoundException;
import com.softserve.edu.bookinglite.exception.PropertyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.softserve.edu.bookinglite.entity.Apartment;
import com.softserve.edu.bookinglite.entity.Property;
import com.softserve.edu.bookinglite.repository.ApartmentRepository;
import com.softserve.edu.bookinglite.repository.PropertyRepository;
import com.softserve.edu.bookinglite.service.dto.ApartmentDto;
import com.softserve.edu.bookinglite.service.mapper.ApartmentMapper;

@Service
public class ApartmentService {

	private final ApartmentRepository apartmentRepository;
	private final PropertyRepository propertyRepository;

	@Autowired
	public ApartmentService(ApartmentRepository apartmentRepository, PropertyRepository propertyRepository) {
		this.apartmentRepository = apartmentRepository;
		this.propertyRepository = propertyRepository;
	}

	public ApartmentDto findApartmentDtoById(Long apartmentId) throws ApartmentNotFoundException {
		Apartment apartment = apartmentRepository.findById(apartmentId)
				.orElseThrow(()-> new ApartmentNotFoundException(apartmentId));
		return ApartmentMapper.instance.toDto(apartment);
	}

	public List<ApartmentDto> findAllApartmentsByPropertyId(Long propertyId) throws PropertyNotFoundException {
		Property property = propertyRepository.findById(propertyId)
				.orElseThrow(() -> new PropertyNotFoundException(propertyId));
		List<ApartmentDto> apartmentDtos = new ArrayList<>();
		for (Apartment apartment : property.getApartments()) {
			ApartmentDto apartmentDto = ApartmentMapper.instance.toDto(apartment);
			apartmentDtos.add(apartmentDto);
		}
		return apartmentDtos;
	}

	public boolean saveApartment(ApartmentDto apartmentDto, Long propertyId, Long userId) throws PropertyNotFoundException {
		Property property = propertyRepository.findById(propertyId)
				.orElseThrow(() -> new PropertyNotFoundException(propertyId));
		if (property.getUser().getId().equals(userId)) {
			Apartment apartment = new Apartment();
			apartment.setName(apartmentDto.getName());
			apartment.setPrice(apartmentDto.getPrice());
			apartment.setNumberOfGuests(apartmentDto.getNumberOfGuests());
			apartment.setApartmentType(apartmentDto.getApartmentType());
			apartment.setAmenities(apartmentDto.getAmenities());
			apartment.setProperty(property);
			apartmentRepository.save(apartment);
			return true;
		}
		return false;
	}

	public boolean updateApartment(ApartmentDto apartmentDto, Long apartmentId, Long userId) throws ApartmentNotFoundException {
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
		}
		return false;
	}

}
