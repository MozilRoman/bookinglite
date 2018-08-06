package com.softserve.edu.bookinglite.service;

import com.softserve.edu.bookinglite.entity.Apartment;
import com.softserve.edu.bookinglite.entity.Property;
import com.softserve.edu.bookinglite.repository.ApartmentRepository;
import com.softserve.edu.bookinglite.repository.PropertyRepository;
import com.softserve.edu.bookinglite.service.dto.ApartmentDto;
import com.softserve.edu.bookinglite.service.mapper.ApartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final PropertyRepository propertyRepository;

    @Autowired
    public ApartmentService(ApartmentRepository apartmentRepository, PropertyRepository propertyRepository) {
        this.apartmentRepository = apartmentRepository;
        this.propertyRepository = propertyRepository;
    }

    public List<ApartmentDto> findAllApartmentsByPropertyId(Long propertyId) {
        Property property = propertyRepository.findById(propertyId).get();
        List<ApartmentDto> apartmentDtos = new ArrayList<>();
        for (Apartment apartment: property.getApartments()) {
            ApartmentDto apartmentDto = ApartmentMapper.instance.toDto(apartment);
            apartmentDtos.add(apartmentDto);
        }
        return apartmentDtos;
    }

    public ApartmentDto findApartmentDtoById(Long id){
        Optional<Apartment> apartment = apartmentRepository.findById(id);
        return apartment.map(ApartmentMapper.instance::toDto).orElse(null);
    }

    public boolean saveApartment (ApartmentDto apartmentDto, Long propertyId, Long userId){
        Property property = propertyRepository.findById(propertyId).get();
        if (property.getUser().getId().equals(userId)){
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

    public boolean updateApartment(ApartmentDto apartmentDto, Long apartmentId, Long userId) {
        Optional<Apartment> apartment = apartmentRepository.findById(apartmentId);
        if (apartment.isPresent() && apartment.get().getProperty().getUser().getId().equals(userId)){
            apartment.get().setName(apartmentDto.getName());
            apartment.get().setPrice(apartmentDto.getPrice());
            apartment.get().setNumberOfGuests(apartmentDto.getNumberOfGuests());
            apartment.get().setApartmentType(apartmentDto.getApartmentType());
            apartment.get().setAmenities(apartmentDto.getAmenities());
            apartmentRepository.save(apartment.get());
            return true;
        }
        return false;
    }

}
