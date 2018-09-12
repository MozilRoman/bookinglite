package com.softserve.edu.bookinglite.exception;

import com.softserve.edu.bookinglite.entity.Booking;

public class ReviewAlreadyExistExeption extends Exception {
  public ReviewAlreadyExistExeption(){
        super("This booking has review...You can write only one review for this booking");
    }
}
