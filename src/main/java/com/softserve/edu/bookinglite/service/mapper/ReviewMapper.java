package com.softserve.edu.bookinglite.service.mapper;

import com.softserve.edu.bookinglite.entity.Review;
import com.softserve.edu.bookinglite.service.dto.ReviewDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ReviewMapper {

    ReviewMapper instance = Mappers.getMapper(ReviewMapper.class);
    ReviewDto toDto(Review review);

}
