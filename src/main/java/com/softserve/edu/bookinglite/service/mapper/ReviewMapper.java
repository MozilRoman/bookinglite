package com.softserve.edu.bookinglite.service.mapper;

import com.softserve.edu.bookinglite.entity.Review;
import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.service.dto.ReviewDto;
import com.softserve.edu.bookinglite.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReviewMapper {

    ReviewMapper instance = Mappers.getMapper(ReviewMapper.class);
    @Mappings({@Mapping(source = "booking.user", target = "userDto")})
    ReviewDto reviewToBaseReviewDto(Review review);

    default UserDto map(User user){
        return UserMapper.instance.UserToBaseUserDtoWithName(user);
    }

}
