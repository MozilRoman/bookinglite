package com.softserve.edu.bookinglite.service.mapper;

import com.softserve.edu.bookinglite.entity.Amenity;
import com.softserve.edu.bookinglite.entity.Apartment;
import com.softserve.edu.bookinglite.entity.ApartmentType;
import com.softserve.edu.bookinglite.entity.Property;
import com.softserve.edu.bookinglite.service.dto.ApartmentDto;
import com.softserve.edu.bookinglite.service.dto.PropertyDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper
public interface ApartmentMapper {

    ApartmentMapper instance = Mappers.getMapper(ApartmentMapper.class);
    ApartmentDto toDto(Apartment apartment);
    Set<Amenity> map(Set<Amenity> amenities);
    Amenity map(Amenity amenity);
    ApartmentType map(ApartmentType apartmentType);

}
