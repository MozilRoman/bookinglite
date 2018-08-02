package com.softserve.edu.bookinglite.service.mapper;

import com.softserve.edu.bookinglite.entity.Apartment;
import com.softserve.edu.bookinglite.service.dto.ApartmentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ApartmentMapper {

    ApartmentMapper instance = Mappers.getMapper(ApartmentMapper.class);
    ApartmentDto toDto(Apartment apartment);

}
