package com.softserve.edu.bookinglite.service;


import com.softserve.edu.bookinglite.entity.Apartment;
import com.softserve.edu.bookinglite.entity.Booking;
import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.repository.BookingRepository;
import com.softserve.edu.bookinglite.repository.BookingStatusRepository;
import com.softserve.edu.bookinglite.service.dto.ApartmentDto;
import com.softserve.edu.bookinglite.service.dto.BookingDto;
import com.softserve.edu.bookinglite.service.mapper.BookingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BookingService {

	final String RESERVED = "Reserved";
	final String CANCELED = "Canceled";

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
//if booking already exist it will return true
    @Transactional
	public boolean checkBookingIfExistByChekInandCheckOut(Long apartment_id, Date checkIn, Date checkOut) {
		if(bookingRepository.getBookingByCheck(apartment_id,setHourAndMinToDate(checkIn,16,0))==null &&
				bookingRepository.getBookingByCheck(apartment_id,setHourAndMinToDate(checkOut,14,0))==null){
		    return false;
        }
        return true;
	}

	@Transactional
	public boolean createBooking(BookingDto bookingDto, Long user_id, Long apartment_id) {
  		if(checkValidationDate (bookingDto)==false){
			System.out.println("validate date : false");
			return false;
  		}
		if (checkBookingIfExistByChekInandCheckOut(apartment_id,bookingDto.getCheck_in(),bookingDto.getCheck_out())==false) {
            Booking booking = new Booking();
            if (apartmentService.findApartmentDtoById(apartment_id) != null) {
                Apartment apartment = new Apartment();
                apartment.setId(apartment_id);
                booking.setApartment(apartment);
            }
            if (userService.findById(user_id) != null) {
                User user = new User();
                user.setId(user_id);
                booking.setUser(user); }
            booking.setCheck_in(setHourAndMinToDate(bookingDto.getCheck_in(),16,0));
            booking.setCheck_out(setHourAndMinToDate(bookingDto.getCheck_out(),14,0));
            booking.setTotal_price(bookingDto.getTotal_price());
            booking.setBookingstatus(bookingStatusRepository.findByName(RESERVED));
            Booking result = bookingRepository.save(booking);
            if (result != null){
                return true;
            }
            else return false;
  		}
  		else {
            System.out.println("validation failed");
            return false;
        }
	}
	
	@Transactional
    public boolean cancelBooking(Long id){
		Booking booking = bookingRepository.findById(id).get();
		if( booking.getCheck_in().after(new Date())   ) {
			booking.setBookingstatus(bookingStatusRepository.findByName(CANCELED));
			return true;   
		}
		else if(booking.getCheck_in().compareTo(new Date())==0
				&& booking.getCheck_in().before(setHourAndMinToDate(new Date(),16,0))) {
			booking.setBookingstatus(bookingStatusRepository.findByName(CANCELED));
			return true;  
		}
		else return false;        		  	       	 	  	    	    		
    }
    @Transactional
	public List<BookingDto> getAllBookingsDtoByOwnerId(Long id_user_owner){
		List<BookingDto> listBookingDto = new ArrayList<>();
		List<Booking> listBooking = bookingRepository.getAllBookingsByOwnerId(id_user_owner);
		if (listBooking.size() > 0) {
			for (Booking booking : listBooking	) {
				BookingDto bookingDto = BookingMapper.instance.bookingToBaseBookingDto(booking);
				listBookingDto.add(bookingDto);
			}
		}
		return listBookingDto;
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
    public Date setHourAndMinToDate(Date date,int hour,int minuts){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE,minuts);
		return calendar.getTime();
	}
}
