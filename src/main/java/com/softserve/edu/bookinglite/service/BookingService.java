package com.softserve.edu.bookinglite.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.softserve.edu.bookinglite.repository.*;
import com.softserve.edu.bookinglite.service.dto.BookingDto;

import com.softserve.edu.bookinglite.service.mapper.BookingMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.softserve.edu.bookinglite.entity.Apartment;
import com.softserve.edu.bookinglite.entity.Booking;
import com.softserve.edu.bookinglite.entity.User;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

	final String RESERVED = "Reserved";

	private final BookingRepository bookingRepository;
	private final ApartmentService apartmentService;
	private final UserService userService;
	private final BookingStatusRepository bookingStatusRepository;

	@Autowired
	public BookingService(BookingRepository bookingRepository,
						  ApartmentService apartmentService,
						  UserService userService,
						  BookingStatusRepository bookingStatusRepository
	) {
		this.bookingRepository = bookingRepository;
		this.apartmentService = apartmentService;
		this.userService = userService;
		this.bookingStatusRepository = bookingStatusRepository;
	}

	@Transactional
	public boolean existsById(Long id) {
		return bookingRepository.existsById(id);
	}

	@Transactional
	public List<BookingDto> findAllBookingDto() { 
		List<BookingDto> allBookingDto = new ArrayList<>();
		for (Booking booking: findAllBookings()) {
			BookingDto bookingDto = BookingMapper.instance.bookingToBaseBookingDto(booking);
			allBookingDto.add(bookingDto);
		}
		return allBookingDto;
	}

	@Transactional
	public List<Booking> findAllBookings() { 
		return bookingRepository.findAll();
	}

	@Transactional
	public BookingDto findBookinDTOById(Long id) { //rename findtAllBookingDtoById
		Optional<Booking> booking = bookingRepository.findById(id);
		return booking.map(BookingMapper.instance::bookingToBaseBookingDto).orElse(null);
	}

	@Transactional
	public List<BookingDto> getAllBookingsDtoByUserId(Long user_id) {
		List<BookingDto> listBookingDto = new ArrayList<>();
		List<Booking> listBooking = bookingRepository.getAllByUserIdOrderByCheck_inAsc(user_id);
		if (listBooking.size() > 0) {
			for (Booking booking : listBooking	) {
				BookingDto bookingDto = BookingMapper.instance.bookingToBaseBookingDto(booking);
				listBookingDto.add(bookingDto);				
			}
		}
		return listBookingDto;
	}

	public Apartment chekBookingByChekInandCheckOut(Long apartment_id, Date chekIn, Date checkOut) {
		return bookingRepository.getBookingByCheck_inAndAndCheck_out(apartment_id, chekIn, checkOut);
	}

	@Transactional
	public boolean createBooking(BookingDto bookingDto, Long user_id, Long apartment_id) {
		if (chekBookingByChekInandCheckOut(apartment_id, bookingDto.getCheck_in(), bookingDto.getCheck_out()) == null) {
						
			if(checkValidationDate (bookingDto)) { 				
					Booking booking = new Booking();
					if (apartmentService.findApartmentDtoById(apartment_id) != null) {
						Apartment apartment = new Apartment();
						apartment.setId(apartment_id);
						booking.setApartment(apartment);
					}
					if (userService.findById(user_id) != null) {
						User user = new User();
						user.setId(user_id);
						booking.setUser(user);
					}
					booking.setCheck_in(bookingDto.getCheck_in());
					booking.setCheck_out(bookingDto.getCheck_out()); //TODO edit visualization Data!!!!!!
					booking.setTotal_price(bookingDto.getTotal_price());
					booking.setBookingstatus(bookingStatusRepository.findByName(RESERVED));
					Booking result = bookingRepository.save(booking);
					if (result != null){ return true;}
					else return false;
			    }
				else{
					return false;
			 	     }			
		} else return false;
	}
	
	@Transactional
    public boolean updateBooking(BookingDto bookingDto){
    	Booking booking = bookingRepository.findById(bookingDto.getBooking_id()).get();
    	    	
    		if (chekBookingByChekInandCheckOut(bookingDto.getApartmentDto().getId(),
    				bookingDto.getCheck_in(), bookingDto.getCheck_out()) == null) {
    			
    			if(checkValidationDate (bookingDto)) {
    				if (apartmentService.findApartmentDtoById(bookingDto.getApartmentDto().getId()) != null) {
						Apartment apartment = new Apartment();
						apartment.setId(bookingDto.getApartmentDto().getId());
						booking.setApartment(apartment);
					}
    				if (userService.findById(bookingDto.getUserDto().getId()) != null) {
						User user = new User();
						user.setId(bookingDto.getUserDto().getId());
	    	        	booking.setUser(user);
					}
    	        	booking.setCheck_in(bookingDto.getCheck_in());
    	        	booking.setCheck_out(bookingDto.getCheck_out());
    	        	booking.setTotal_price(bookingDto.getTotal_price());   
    	        	booking.setBookingstatus(bookingStatusRepository.findById(bookingDto.getBookingstatus().getId()).get());
    			}
    			else return false;
    		} 
    		else if( bookingDto.getBookingstatus().getId() != booking.getBookingstatus().getId()) {
    			booking.setBookingstatus(bookingStatusRepository.findById(bookingDto.getBookingstatus().getId()).get());
    		}
    		else return false;
    		
    	bookingRepository.save(booking);
    	return true;    	  	       	 	  	    	    		
    }    
    
    public boolean checkValidationDate (BookingDto bookingDto){
    	boolean validation= true;
    	
    	if(bookingDto.getCheck_out().before(bookingDto.getCheck_in())) {
    		validation= false;
    	}
    	if(bookingDto.getCheck_in().before(new Date()) ) {
    		validation= false;
    	}
    	if(bookingDto.getCheck_out().compareTo(bookingDto.getCheck_in())==0) {
    		validation= false;
    	}  	
    	return validation;
    }
}
