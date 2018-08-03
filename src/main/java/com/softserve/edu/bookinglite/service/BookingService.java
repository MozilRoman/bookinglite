package com.softserve.edu.bookinglite.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.softserve.edu.bookinglite.repository.*;
import com.softserve.edu.bookinglite.service.dto.BookingDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.softserve.edu.bookinglite.entity.Apartment;
import com.softserve.edu.bookinglite.entity.Booking;
import com.softserve.edu.bookinglite.entity.BookingStatus;
import com.softserve.edu.bookinglite.entity.Property;
import com.softserve.edu.bookinglite.entity.Review;
import com.softserve.edu.bookinglite.entity.User;

@Service
public class BookingService {
	
	final String RESERVED= "Reserved";
	
	 private final BookingRepository bookingRepository;
	 private final ApartmentRepository apartmentRepository;	 
	 private final UserRepository userRepository;
	 private final BookingStatusRepository bookingStatusRepository;
	 private final ReviewRepository reviewRepository;

	    @Autowired
	    public BookingService(BookingRepository bookingRepository,
	    		ApartmentRepository apartmentRepository,
	    		UserRepository userRepository,
	    		BookingStatusRepository bookingStatusRepository,
	    		ReviewRepository reviewRepository
	    		) {
	        this.bookingRepository = bookingRepository;
	        this.apartmentRepository = apartmentRepository;
	        this.userRepository = userRepository;
	        this.bookingStatusRepository = bookingStatusRepository;
	        this.reviewRepository = reviewRepository;
	}

	
	public boolean existsById(Long id){
        return bookingRepository.existsById(id);
    }
	
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
	
	public List<Booking> getAllBooking(){
		System.out.println("let all booking 222");
		return bookingRepository.findAll();
	}
	
	public BookingDto getBookinDTOById(Long id){
		System.out.println("Id1 = "+ id);
		BookingDto bookingDto = convertToBookingDto(getBookingById(id));
		return bookingDto;
	}
	
	public Booking getBookingById(Long id){
		System.out.println("Id2 = "+ id);

		Optional<Booking> booking = bookingRepository.findById(id);
		if(booking.isPresent()) {
			return booking.get();
		}
		return new Booking();
	}
	
	/*public Booking validation(BookingDto bookingDto, Long booking_id){
		Apartment apartment= bookingDto.getApartment();
		Property property=apartment.getProperty();
		System.out.println("111111111111111111111 ");
		Optional<Booking> booking= bookingRepository.findBookingByQuery(booking_id, 1L);
		System.out.println("booking price by query = "+ booking.get().getTotal_price());
		return booking.get();
	}*/
	
	//TODO: REFACTOR  use method convert to Booking(bookingDto)
	public boolean createBooking(BookingDto bookingDto, Long user_id, Long apartment_id){
    	Booking booking=new Booking();  	
    	
    	Optional<Apartment> apartment= apartmentRepository.findById(apartment_id);
			booking.setApartment(apartment.get());			
			
		Optional<User> user= userRepository.findById(user_id);
			booking.setUser(user.get());		
				
    	booking.setCheck_in(bookingDto.getCheck_in());
    	booking.setCheck_out(bookingDto.getCheck_out()); //TODO edit visualization Data!!!!!!
    	booking.setTotal_price(bookingDto.getTotal_price());
    	booking.setBookingstatus(bookingStatusRepository.findByName(RESERVED));
    	
    	if(bookingDto.getReview()!=null) {
    		Review review = bookingDto.getReview();
        	reviewRepository.save(review);
        	booking.setReview(bookingDto.getReview());        	
    	}    	    	
    	
    	//check if date is not reserved 
    	
    	Booking result = bookingRepository.save(booking); 
        if(result != null)return true;
        else return false;
    }

	
	
    public boolean updateBooking(BookingDto bookingDto){
    	    	    	
    	Booking booking=new Booking();
    	booking.setId(bookingDto.getId());
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
        else return false;
    }
    
    private BookingDto convertToBookingDto(Booking booking){
    	BookingDto bookingDto = new BookingDto();
    	bookingDto.setId(booking.getId());
    	bookingDto.setApartment(booking.getApartment());
    	bookingDto.setBookingstatus(booking.getBookingstatus());
    	bookingDto.setCheck_in(booking.getCheck_in());
    	bookingDto.setCheck_out(booking.getCheck_out());
    	bookingDto.setTotal_price(booking.getTotal_price());
    	bookingDto.setUser(userRepository. findById( booking.getUser().getId()).get());
    	System.out.println("1111 ");
    	
    	try{
    		Optional<Review> review= reviewRepository.findById( booking.getReview().getId());
    		System.out.println("2222 ");
        	if(review.isPresent()){
        		System.out.println("33333 ");
        		bookingDto.setReview(review.get()); } 
        	System.out.println("2222 ");
    	}
    	catch(NullPointerException e) {
    		System.out.println("55555 ");
    	} 	    	
    	
    	/*if(reviewRepository.findById( booking.getReview().getId()).get()!=null) {
			bookingDto.setReview(reviewRepository.findById(booking.getReview().getId()).get());
		}*/
        return bookingDto;
        }
}
