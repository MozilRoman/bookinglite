package com.softserve.edu.bookinglite.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserHasBookingsDto {
    private UserDto userDto;
    private List<BookingDto> bookingDtos;

    public UserHasBookingsDto(UserDto userDto, List<BookingDto> bookingDtos) {
        this.userDto = userDto;
        this.bookingDtos = bookingDtos;
    }
    public UserHasBookingsDto(UserDto userDto) {
        this.userDto = userDto;
        this.bookingDtos = new ArrayList<>();
    }

    public void addBookingDto(BookingDto bookingDto){
        bookingDtos.add(bookingDto);
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public List<BookingDto> getBookingDtos() {
        return bookingDtos;
    }

    public void setBookingDtos(List<BookingDto> bookingDtos) {
        this.bookingDtos = bookingDtos;
    }
}
