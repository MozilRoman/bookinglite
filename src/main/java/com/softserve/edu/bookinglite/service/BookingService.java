package com.softserve.edu.bookinglite.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.softserve.edu.bookinglite.repository.*;
import com.softserve.edu.bookinglite.service.dto.BookingDto;

import com.softserve.edu.bookinglite.service.dto.UserHasBookingsDto;
import com.softserve.edu.bookinglite.service.mapper.BookingMapper;
import com.softserve.edu.bookinglite.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.softserve.edu.bookinglite.entity.Apartment;
import com.softserve.edu.bookinglite.entity.Booking;
import com.softserve.edu.bookinglite.entity.BookingStatus;
import com.softserve.edu.bookinglite.entity.Property;
import com.softserve.edu.bookinglite.entity.Review;
import com.softserve.edu.bookinglite.entity.User;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {
	
	final String RESERVED= "Reserved";
	
	 private final BookingRepository bookingRepository;
	 private final ApartmentService apartmentService;
	 private final UserService userService;
	 private final BookingStatusRepository bookingStatusRepository;
	 private final ReviewRepository reviewRepository;

	    @Autowired
	    public BookingService(BookingRepository bookingRepository,
							  ApartmentService apartmentService,
	    		UserService userService,
	    		BookingStatusRepository bookingStatusRepository,
	    		ReviewRepository reviewRepository
	    		) {
	        this.bookingRepository = bookingRepository;
	        this.apartmentService = apartmentService;
	        this.userService=userService;
	        this.bookingStatusRepository = bookingStatusRepository;
	        this.reviewRepository = reviewRepository;
	}

@Transactional
	public boolean existsById(Long id){
        return bookingRepository.existsById(id);
    }
	@Transactional
	public List<BookingDto> getAllBookingDto(){
		System.out.println("let all booking ");
		List<Booking> listBooking= getAllBooking();
		List<BookingDto> listBookingDto= new ArrayList<>();
		for (int i=0; i<listBooking.size(); i++){
			Booking booking = listBooking.get(i);
            BookingDto bookingDto = convertToBookingDto(booking);
            listBookingDto.add(bookingDto);
        }
		return listBookingDto;
	}
	@Transactional
	public List<Booking> getAllBooking(){
		return bookingRepository.findAll();
	}

	/*@Transactional
	public  BookingDto getBookinDTOById(Long id){
		BookingDto bookingDto = bookingRepository.findById(id).map(BookingMapper.instance::bookingToBaseBookingDto).orElse(new BookingDto());
		return bookingDto;
	}*/
	@Transactional
	public BookingDto getBookinDTOById(Long id){
		BookingDto bookingDto = convertToBookingDto(getBookingById(id));
		return bookingDto;
	}
	@Transactional
	public Booking getBookingById(Long id){
		System.out.println("Id2 = "+ id);

		Optional<Booking> booking = bookingRepository.findById(id);
		if(booking.isPresent()) {
			return booking.get();
		}
		return new Booking();
	}
	@Transactional
	public UserHasBookingsDto getAllBookingsDtoByUserId(Long user_id){
		List<Booking> listBooking= bookingRepository.getAllByUserIdOrderByCheck_inAsc(user_id);
		UserHasBookingsDto userHasBookingsDto=new UserHasBookingsDto(userService.findById(user_id));
		if(listBooking.size()>0) {
			for (Booking booking : listBooking
					) {
      userHasBookingsDto.addBookingDto(convertToBookingDto(booking));
			}
		}
		return userHasBookingsDto;
	}
	
	/*public Booking validation(BookingDto bookingDto, Long booking_id){
		Apartment apartment= bookingDto.getApartment();
		Property property=apartment.getProperty();
		System.out.println("111111111111111111111 ");
		Optional<Booking> booking= bookingRepository.findBookingByQuery(booking_id, 1L);
		System.out.println("booking price by query = "+ booking.get().getTotal_price());
		return booking.get();
	}*/
	
	@Transactional
	public boolean createBooking(BookingDto bookingDto, Long user_id, Long apartment_id){
    	Booking booking=new Booking();  	
    	
    	if(apartmentService.findApartmentDtoById(apartment_id)!=null){
    		Apartment apartment=new Apartment();
    		apartment.setId(apartment_id);
    		booking.setApartment(apartment);
		}

		if(userService.findById(user_id)!=null){
    		User user=new User();
    		user.setId(user_id);
    		booking.setUser(user);
		}

    	booking.setCheck_in(bookingDto.getCheck_in());
    	booking.setCheck_out(bookingDto.getCheck_out()); //TODO edit visualization Data!!!!!!
    	booking.setTotal_price(bookingDto.getTotal_price());
    	booking.setBookingstatus(bookingStatusRepository.findByName(RESERVED));
    	//check if date is not reserved 
    	
    	Booking result = bookingRepository.save(booking); 
        if(result != null)return true;
        else return false;
    }

	
	
    public boolean updateBooking(BookingDto bookingDto){
    	    	    	
    	/*Booking booking=new Booking();
    	booking.setId(bookingDto.getId());
    	Apartment apartment= new Apartment();
    	apartment.
    	booking.setApartment(bookingDto.getApartmentDto());
    	booking.setUser(bookingDto.getUser());
    	booking.setCheck_in(bookingDto.getCheck_in());
    	booking.setCheck_out(bookingDto.getCheck_out());
    	booking.setTotal_price(bookingDto.getTotal_price());
    	booking.setBookingstatus(bookingDto.getBookingstatus());
    	booking.setReview(bookingDto.getReview());
    	
    	
    	//check if date is not reserved 
    	
    	Booking result = bookingRepository.save(booking);
        if(result != null)return true;
        else return false;*/
    	return true;
    }
    @Transactional
    public BookingDto convertToBookingDto(Booking booking){
    	BookingDto bookingDto = new BookingDto();
    	bookingDto.setBooking_id(booking.getId());
    	bookingDto.setTotal_price(booking.getTotal_price());
    	bookingDto.setCheck_in(booking.getCheck_in());
    	bookingDto.setCheck_out(booking.getCheck_out());
    	bookingDto.setUserDto(userService.findById(booking.getUser().getId()));
    	bookingDto.setApartmentDto(apartmentService.findApartmentDtoById(booking.getApartment().getId()));

        return bookingDto;
        }
}
