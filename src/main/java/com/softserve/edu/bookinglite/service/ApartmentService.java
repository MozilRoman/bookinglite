package com.softserve.edu.bookinglite.service;

import com.softserve.edu.bookinglite.dto.ApartmentDto;
import com.softserve.edu.bookinglite.entity.Apartment;
import com.softserve.edu.bookinglite.entity.Property;
import com.softserve.edu.bookinglite.repository.ApartmentRepository;
import com.softserve.edu.bookinglite.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApartmentService {

    @Autowired
    ApartmentRepository apartmentRepository;
    @Autowired
    PropertyRepository propertyRepository;

    public List<Apartment> findAllApartment(){
        return apartmentRepository.findAll();
    }

    public List<ApartmentDto> findAllApartmentDto(){
        List<Apartment> allApartments = findAllApartment();
        List<ApartmentDto> allApartmentsDto = new ArrayList<>();
        for (int i=0; i<allApartments.size(); i++){
            Apartment apartment = allApartments.get(i);
            ApartmentDto apartmentDto = convertToDto(apartment);
            allApartmentsDto.add(apartmentDto);
        }
        return allApartmentsDto;
    }

    public Apartment findById(Long id){
        Optional<Apartment> apartment = apartmentRepository.findById(id);
        if(apartment.isPresent()){
            return apartment.get();
        } else {
            return null;
        }
    }

    public ApartmentDto findDtoById(Long id){
        Optional<Apartment> apartment = apartmentRepository.findById(id);
        if(apartment.isPresent()){
            ApartmentDto apartmentDto = new ApartmentDto();
            apartmentDto.setName(apartment.get().getName());
            apartmentDto.setPrice(apartment.get().getPrice());
            apartmentDto.setNumberOfGuests(apartment.get().getNumberOfGuests());
            apartmentDto.setApartmentType(apartment.get().getApartmentType());
            apartmentDto.setAmenities(apartment.get().getAmenities());
            return apartmentDto;
        } else {
            return null;
        }
    }

    public boolean saveApartment (ApartmentDto apartmentDto, Long propertyId, Long userId){
        Property property = propertyRepository.findById(propertyId).get();
        if (property != null && property.getUser().getId().equals(userId)){
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

    public boolean updateApartment(ApartmentDto apartmentDto, Long apartmentId) {
        if (apartmentId != null){
            Apartment apartment = new Apartment();
            apartment.setId(apartmentId);
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

    private ApartmentDto convertToDto(Apartment apartment){
        ApartmentDto apartmentDto = new ApartmentDto();
        apartmentDto.setId(apartment.getId());
        apartmentDto.setName(apartment.getName());
        apartmentDto.setPrice(apartment.getPrice());
        apartmentDto.setNumberOfGuests(apartment.getNumberOfGuests());
        apartmentDto.setApartmentType(apartment.getApartmentType());
        apartmentDto.setAmenities(apartment.getAmenities());
        return apartmentDto;
    }

}
