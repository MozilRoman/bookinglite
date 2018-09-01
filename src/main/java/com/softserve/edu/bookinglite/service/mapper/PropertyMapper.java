package com.softserve.edu.bookinglite.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.softserve.edu.bookinglite.entity.Address;
import com.softserve.edu.bookinglite.entity.Apartment;
import com.softserve.edu.bookinglite.entity.Property;
import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.service.dto.ApartmentDto;
import com.softserve.edu.bookinglite.service.dto.CreatePropertyDto;
import com.softserve.edu.bookinglite.service.dto.PropertyDto;
import com.softserve.edu.bookinglite.service.dto.UserDto;

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
    public abstract PropertyDto propertyToBasePropertyDtoWithAddress(Property property);


    @Mappings({@Mapping(target = "apartments",ignore = true),
            @Mapping(target = "address",ignore = true)})
    PropertyDto propertyToBasePropertyDtoWithUser(Property property);

    @Mapping(target = "user",ignore = true)
    PropertyDto propertyToBasePropertyDtoWithAddressApartment(Property property);

    PropertyDto propertyToBasePropertyDtoWithApartmentAddressUser(Property property);

    Property toEntity(PropertyDto propertyDto);
    
    @Mappings({
    @Mapping(source = "propertyTypeId", target = "propertyType.id"),
    @Mapping(source = "countryId", target = "address.city.country.id"),
    @Mapping(source = "cityId", target = "address.city.id"),
    @Mapping(source = "addressLine", target = "address.addressLine"),
    @Mapping(source = "zip", target = "address.zip"),
    })
    Property toEntityFromCreatePropertyDto(CreatePropertyDto createPropertyDto);
    
    List<ApartmentDto> map(List<ApartmentDto> apartments);

    default ApartmentDto map(Apartment apartment) {
        return ApartmentMapper.instance.toDtoWithOutPropertyDto(apartment);
    }
    Address map(Address address);
    default UserDto map(User user){
        return UserMapper.instance.UserToBaseUserDto(user);
    }
}