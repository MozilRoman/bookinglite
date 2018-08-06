package com.softserve.edu.bookinglite.service.mapper;

import com.softserve.edu.bookinglite.entity.Address;
import com.softserve.edu.bookinglite.entity.Apartment;
import com.softserve.edu.bookinglite.entity.Property;
import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.service.dto.PropertyDto;
import com.softserve.edu.bookinglite.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PropertyMapper {

	PropertyMapper instance = Mappers.getMapper(PropertyMapper.class);
	
	@Mappings({@Mapping(target = "apartments",ignore = true),
        @Mapping(target = "address",ignore = true),
        @Mapping(target = "user",ignore = true)})
	PropertyDto propertyToBasePropertyDto(Property property);
	
	@Mappings({@Mapping(target = "address",ignore = true),
        @Mapping(target = "user",ignore = true)})
	PropertyDto propertyToBasePropertyDtoWithApartment(Property property);
	
	@Mappings({@Mapping(target = "apartments",ignore = true),
        @Mapping(target = "user",ignore = true)})
	PropertyDto propertyToBasePropertyDtoWithAddress(Property property);
	
	
	@Mappings({@Mapping(target = "apartments",ignore = true),
        @Mapping(target = "address",ignore = true)})
	PropertyDto propertyToBasePropertyDtoWithUser(Property property);
	
	@Mapping(target = "user",ignore = true)
	PropertyDto propertyToBasePropertyDtoWithAddressApartment(Property property);
	
	
	PropertyDto propertyToBasePropertyDtoWithApartmentAddressUser(Property property);
	
	Set<Apartment> map(Set<Apartment> apartments);
	Address map(Address address);
	default UserDto map(User user){
		return UserMapper.instance.UserToBaseUserDto(user);
	}
}