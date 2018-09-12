package com.softserve.edu.bookinglite.service.mapper;

import com.softserve.edu.bookinglite.entity.Review;
import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.service.dto.ReviewDto;
import com.softserve.edu.bookinglite.service.dto.UserDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Stream;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {

    ReviewMapper instance = Mappers.getMapper(ReviewMapper.class);

    @Mappings({
            @Mapping(source = "booking.user", target = "userDto")})
    ReviewDto reviewToBaseReviewDto(Review review);

    List<ReviewDto> reviewsToReviewDto(Stream<Review> reviews);

    default UserDto map(User user) {
        return UserMapper.instance.UserToBaseUserDtoWithName(user);
    }

    default Page<ReviewDto> toPageReviewDto(Page<Review> page) {
        return page.map(this::reviewToBaseReviewDto);
    }

}
