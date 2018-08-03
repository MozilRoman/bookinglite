package com.softserve.edu.bookinglite.service.mapper;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.softserve.edu.bookinglite.entity.Address;
import com.softserve.edu.bookinglite.entity.Apartment;
import com.softserve.edu.bookinglite.entity.Property;
import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.service.dto.PropertyDto;
import com.softserve.edu.bookinglite.service.dto.UserDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PropertyMapper {

	PropertyMapper instance = Mappers.getMapper(PropertyMapper.class);
	
	@Mappings({@Mapping(target = "apartments",ignore = true),
        @Mapping(target = "address",ignore = true),
        @Mapping(target = "userDto",ignore = true)})
	PropertyDto propertyToBasePropertyDto(Property property);
	
	@Mappings({@Mapping(target = "address",ignore = true),
        @Mapping(target = "userDto",ignore = true)})
	PropertyDto propertyToBasePropertyDtoWithApartment(Property property);
	
	@Mappings({@Mapping(target = "apartments",ignore = true),
        @Mapping(target = "userDto",ignore = true)})
	PropertyDto propertyToBasePropertyDtoWithAddress(Property property);
	
	
	@Mappings({@Mapping(target = "apartments",ignore = true),
        @Mapping(target = "address",ignore = true)})
	PropertyDto propertyToBasePropertyDtoWithUser(Property property);
	
	@Mapping(target = "userDto",ignore = true)
	PropertyDto propertyToBasePropertyDtoWithAddressApartment(Property property);
	
	
	PropertyDto propertyToBasePropertyDtoWithApartmentAddressUser(Property property);
	
	Set<Apartment> map(Set<Apartment> apartments);
	Address map(Address address);
	UserDto map(UserDto userDto);
}
