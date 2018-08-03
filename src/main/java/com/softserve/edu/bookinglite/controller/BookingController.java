package com.softserve.edu.bookinglite.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.edu.bookinglite.service.BookingService;
import com.softserve.edu.bookinglite.service.dto.BookingDto;



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
				
		listBookingDto= bookingService.getAllBookingDto();
		return listBookingDto ;
	}
	
	@GetMapping(value="/booking/{booking_id}")
	public BookingDto getBookingById(@PathVariable ("booking_id") Long booking_id ) {
		return bookingService.getBookinDTOById(booking_id);
	}
	
	/*@PostMapping(value="/bookingbyquery/{booking_id}")
	public BookingDto getBookingByQuery(@Valid @RequestBody BookingDto bookingDto, 
			@PathVariable ("booking_id") Long booking_id ) {
		bookingService.validation(bookingDto, booking_id);
		BookingDto b = new BookingDto();
		return b;
	}*/
	
	@PostMapping(value="/booking/{apartment_id}")
	public ResponseEntity<BookingDto> createBooking(@Valid @RequestBody BookingDto bookingDto, 
			@PathVariable ("apartment_id") Long apartment_id,
			Principal principal) {		 		
		 Long userId = Long.parseLong(principal.getName());						
		 bookingService.createBooking(bookingDto, userId, apartment_id);
		return new ResponseEntity<BookingDto>(HttpStatus.CREATED);
	}
	
	@PutMapping(value="/booking/{id}") 
	public ResponseEntity<BookingDto> updateBooking(@Valid @RequestBody BookingDto bookingDto,  //Or ResponseEntity<Void> ??
			@PathVariable ("id") Long id) {	
		bookingDto.setBooking_id(id);;
		if(bookingService.updateBooking(bookingDto)) {
			return new ResponseEntity<BookingDto>(HttpStatus.OK);
		}else {
			return new ResponseEntity<BookingDto>(HttpStatus.BAD_REQUEST);
              }
	}
}

