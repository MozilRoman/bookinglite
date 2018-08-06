package com.softserve.edu.bookinglite.controller;

import com.softserve.edu.bookinglite.service.BookingService;
import com.softserve.edu.bookinglite.service.dto.BookingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;



@RestController
@RequestMapping("/api")
public class BookingController {
	
	private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
     }
	
	@GetMapping(value="/booking")
	public List<BookingDto> getAllBooking( ) {
		List<BookingDto> listBookingDto= new ArrayList<>();
		listBookingDto= bookingService.findAllBookingDto();
		return listBookingDto ;
	}
	
	@GetMapping(value="/booking/{booking_id}")
	public BookingDto getBookingById(@PathVariable ("booking_id") Long booking_id ) {
		return bookingService.findBookinDTOById(booking_id);
	}
	
	@GetMapping(value="/bookings")
	public List<BookingDto> getAllBookingsDtoByUserId(Principal principal) {
    	Long user_id = Long.parseLong(principal.getName());
		return bookingService.getAllBookingsDtoByUserId(user_id);
	}
	
	@PostMapping(value="/booking/{apartment_id}")
	public ResponseEntity<BookingDto> createBooking(@Valid @RequestBody BookingDto bookingDto, 
			@PathVariable ("apartment_id") Long apartment_id,
			Principal principal) {		 		
		 Long userId = Long.parseLong(principal.getName());						 
		 if(bookingService.createBooking(bookingDto, userId, apartment_id)) {
				return new ResponseEntity<BookingDto>(HttpStatus.CREATED);
			}else {
				return new ResponseEntity<BookingDto>(HttpStatus.BAD_REQUEST);
	              }
	}
	
	@PutMapping(value="/booking/{id}") 
	public ResponseEntity<BookingDto> cancelBooking( @PathVariable ("id") Long id) {	
		if(bookingService.cancelBooking(id)) {
			return new ResponseEntity<BookingDto>(HttpStatus.OK);
		}else {
			return new ResponseEntity<BookingDto>(HttpStatus.BAD_REQUEST);
              }
	}
	
}

