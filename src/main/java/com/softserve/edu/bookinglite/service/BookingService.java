package com.softserve.edu.bookinglite.service;


import com.softserve.edu.bookinglite.entity.Apartment;
import com.softserve.edu.bookinglite.entity.Booking;
import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.repository.BookingRepository;
import com.softserve.edu.bookinglite.repository.BookingStatusRepository;
import com.softserve.edu.bookinglite.service.dto.BookingDto;
import com.softserve.edu.bookinglite.service.dto.CreateBookingDto;
import com.softserve.edu.bookinglite.service.mapper.BookingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
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
	public List<BookingDto> findAllBookingsDtoByUserId(Long user_id) {
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
		if(bookingRepository.getBookingByCheck(apartment_id,setHourAndMinToDate(checkIn,17,0),setHourAndMinToDate(checkIn,17,0))==null &&
				){
		    return false;
        }
        return true;
	}

	@Transactional
	public boolean createBooking(CreateBookingDto createBookingDto, Long user_id, Long apartment_id) {
		if(checkValidationDate (createBookingDto)==false || 
				createBookingDto.getNumberOfGuests() > apartmentService.findApartmentDtoById(apartment_id).getNumberOfGuests()){
			return false;
  		}
		if (checkBookingIfExistByChekInandCheckOut(apartment_id,createBookingDto.getCheck_in(),createBookingDto.getCheck_out())==false) {
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
            booking.setCheck_in(setHourAndMinToDate(createBookingDto.getCheck_in(),17,0));
            booking.setCheck_out(setHourAndMinToDate(createBookingDto.getCheck_out(),15,0));
            booking.setTotal_price(getPriceForPeriod(apartmentService.findApartmentDtoById(apartment_id).getPrice(),
            		createBookingDto.getCheck_in(),createBookingDto.getCheck_out()));
            booking.setBookingstatus(bookingStatusRepository.findByName(RESERVED));
            Booking result = bookingRepository.save(booking);
            if (result != null){
                return true;
            }
            else return false;
  		}
  		else {
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
				&& booking.getCheck_in().before(setHourAndMinToDate(new Date(),17,0))) {
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

    public boolean checkValidationDate (CreateBookingDto bookingDto){
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

    public BigDecimal getPriceForPeriod(BigDecimal priceOneDay, Date checkIn, Date checkOut) {
    	BigDecimal priceForPeriod= new BigDecimal(BigInteger.ZERO,2);
    	int diff=(int)( (checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24));
    	priceOneDay= priceOneDay.multiply( new BigDecimal(diff));
    	return priceForPeriod.add(priceOneDay);
    }
}

