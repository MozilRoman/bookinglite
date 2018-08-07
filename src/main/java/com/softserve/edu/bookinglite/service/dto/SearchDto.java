package com.softserve.edu.bookinglite.service.dto;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class SearchDto {
    @NotNull
    Integer countryId;

    Integer cityId;

    @NotNull
    Date checkIn;
    @NotNull
    Date checkOut;
    @NotNull
    Integer numberOfGuests;

}
