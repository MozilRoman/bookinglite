package com.softserve.edu.bookinglite.controller;

import com.softserve.edu.bookinglite.service.BookingService;
import com.softserve.edu.bookinglite.service.dto.BookingDto;
import com.softserve.edu.bookinglite.service.dto.CreateBookingDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;



@RestController
@RequestMapping("/api")
public class BookingController {
	
	private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
     }
	
    @GetMapping(value="/booking/{bookingId}")
	public BookingDto getBookingById(@PathVariable ("bookingId") Long bookingId ) {
		return bookingService.findBookinDTOById(bookingId);
	}
    
	@GetMapping(value="/bookings")
	public List<BookingDto> getAllBookingsDtoByUserId(Principal principal) {
    	Long userId = Long.parseLong(principal.getName());
		return bookingService.findAllBookingsDtoByUserId(userId);
	}
	
	@GetMapping(value="/guestarivals")
	public List<BookingDto> getAllBookingsDtoByOwnerId(Principal principal) {
		Long userId = Long.parseLong(principal.getName());
		return bookingService.getAllBookingsDtoByOwnerId(userId);
	}
	
	@PostMapping(value="/booking/{apartmentId}")
	public ResponseEntity<CreateBookingDto> createBooking(@Valid @RequestBody CreateBookingDto createBookingDto, 
			@PathVariable ("apartmentId") Long apartmentId,
			Principal principal) {		 		
		 Long userId = Long.parseLong(principal.getName());						 
		 if(bookingService.createBooking(createBookingDto, userId, apartmentId)) {
				return new ResponseEntity<CreateBookingDto>(HttpStatus.CREATED);
			}else {
				return new ResponseEntity<CreateBookingDto>(HttpStatus.BAD_REQUEST);
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

