package com.softserve.edu.bookinglite.service.mapper;

import com.softserve.edu.bookinglite.entity.*;
import com.softserve.edu.bookinglite.service.dto.ApartmentDto;
import com.softserve.edu.bookinglite.service.dto.PropertyDto;
import com.softserve.edu.bookinglite.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper
public interface ApartmentMapper {


    ApartmentMapper instance = Mappers.getMapper(ApartmentMapper.class);

    @Mappings({
            @Mapping(target="propertyDto", source="apartment.property"),
    })
    ApartmentDto toDto(Apartment apartment);

    Set<Amenity> map(Set<Amenity> amenities);

    Amenity map(Amenity amenity);

    ApartmentType map(ApartmentType apartmentType);



    default PropertyDto map(Property property){
        return PropertyMapper.instance.propertyToBasePropertyDtoWithAddressApartment(property);
    }

}