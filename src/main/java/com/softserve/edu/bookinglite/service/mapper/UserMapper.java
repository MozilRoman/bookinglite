package com.softserve.edu.bookinglite.service.mapper;

import com.softserve.edu.bookinglite.entity.Address;
import com.softserve.edu.bookinglite.entity.Role;
import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Set;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper instance = Mappers.getMapper(UserMapper.class);

    @Mappings({@Mapping(target = "roles",ignore = true),
            @Mapping(target = "address",ignore = true)})
    UserDto UserToBaseUserDto(User user);

    @Mappings({@Mapping(target = "roles",ignore = true),
            @Mapping(target = "address",ignore = true),
    @Mapping(target = "email", ignore = true),
    @Mapping(target = "id", ignore = true),
    @Mapping(target = "phone_number", ignore = true)})
    UserDto UserToBaseUserDtoWithName(User user);

    @Mappings({@Mapping(target = "address",ignore = true)})
    UserDto UserToBaseUserDtoWithRoles(User user);

    @Mappings({@Mapping(target = "roles",ignore = true)})
    UserDto UserToBaseUserDtoWithAddress(User user);

    UserDto UserToBaseUserDtoWithRolesAndAddress(User user);


    Address map(Address address);

    Set<Role> map(Set<Role> roles);



    default org.springframework.security.core.userdetails.User UsertoSecurityUser(User user){
        return new org.springframework.security.core.userdetails.User(user.getId().toString(),user.getPassword(),new ArrayList<>());
    }
}
