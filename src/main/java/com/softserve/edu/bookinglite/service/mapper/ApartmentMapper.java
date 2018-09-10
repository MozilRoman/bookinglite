package com.softserve.edu.bookinglite.service.mapper;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.softserve.edu.bookinglite.entity.Amenity;
import com.softserve.edu.bookinglite.entity.Apartment;
import com.softserve.edu.bookinglite.entity.ApartmentType;
import com.softserve.edu.bookinglite.entity.Property;
import com.softserve.edu.bookinglite.service.dto.ApartmentDto;
import com.softserve.edu.bookinglite.service.dto.CreateApartmentDto;
import com.softserve.edu.bookinglite.service.dto.PropertyDto;

@Mapper
public interface ApartmentMapper {

	ApartmentMapper instance = Mappers.getMapper(ApartmentMapper.class);

	@Mappings({ @Mapping(target = "propertyDto", source = "apartment.property"), })
	ApartmentDto toDto(Apartment apartment);

	ApartmentDto toDtoWithOutPropertyDto(Apartment apartment);

	@Mappings({ @Mapping(source = "apartmentTypeId", target = "apartmentType.id") })
	Apartment toEntity(CreateApartmentDto createApartmentDto);

	Set<Amenity> map(Set<Amenity> amenities);

	Amenity map(Amenity amenity);

	ApartmentType map(ApartmentType apartmentType);

	default PropertyDto map(Property property) {
		return PropertyMapper.instance.propertyToBasePropertyDtoWithAddress(property);
	}

}