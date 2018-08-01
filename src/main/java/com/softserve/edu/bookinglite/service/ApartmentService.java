package com.softserve.edu.bookinglite.service;

import ch.qos.logback.core.sift.AppenderFactory;
import com.softserve.edu.bookinglite.dto.ApartmentDto;
import com.softserve.edu.bookinglite.entity.Apartment;
import com.softserve.edu.bookinglite.entity.Property;
import com.softserve.edu.bookinglite.repository.ApartmentRepository;
import com.softserve.edu.bookinglite.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
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
        List<ApartmentDto> allApartmentsDto = new ArrayList<>();
        for (Apartment apartment: findAllApartment()) {
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
            apartmentDto.setId(apartment.get().getId());
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

    public boolean updateApartment(ApartmentDto apartmentDto, Long apartmentId, Long userId) {
        Optional<Apartment> apartment = apartmentRepository.findById(apartmentId);
        if (apartment.isPresent()){
            apartment.get().setName(apartmentDto.getName());
            apartment.get().setPrice(apartmentDto.getPrice());
            apartment.get().setNumberOfGuests(apartmentDto.getNumberOfGuests());
            apartment.get().setApartmentType(apartmentDto.getApartmentType());
            apartment.get().setAmenities(apartmentDto.getAmenities());
            if ((apartment.get().getProperty().getUser().getId()).equals(userId)){
                apartmentRepository.save(apartment.get());
                return true;
            }
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
