package com.softserve.edu.bookinglite.service.mapper;

import com.softserve.edu.bookinglite.entity.Apartment;
import com.softserve.edu.bookinglite.entity.Booking;
import com.softserve.edu.bookinglite.entity.Review;
import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.service.dto.ApartmentDto;
import com.softserve.edu.bookinglite.service.dto.BookingDto;
import com.softserve.edu.bookinglite.service.dto.ReviewDto;
import com.softserve.edu.bookinglite.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookingMapper {

    BookingMapper instance = Mappers.getMapper(BookingMapper.class);
    
    @Mappings({
    	@Mapping(target="booking_id", source="booking.id"),
        @Mapping(target="apartmentDto", source="booking.apartment"),
        @Mapping(target="userDto", source="booking.user")
      })
    BookingDto bookingToBaseBookingDto(Booking booking);
    
    ApartmentDto apartmentToApartmentDTO(Apartment apartment);
    default UserDto map(User user){
        return UserMapper.instance.UserToBaseUserDto(user);
    }

    default ReviewDto map(Review review){
        return ReviewMapper.instance.toDto(review);
    }

   // ApartmentDto map(Apartment apartment);
    //UserDto map(User user);

}
