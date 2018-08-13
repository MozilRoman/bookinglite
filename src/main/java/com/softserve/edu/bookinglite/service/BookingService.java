package com.softserve.edu.bookinglite.service;


import com.softserve.edu.bookinglite.entity.Apartment;
import com.softserve.edu.bookinglite.entity.Booking;
import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.exception.ApartmentNotFoundException;
import com.softserve.edu.bookinglite.exception.BookingCancelException;
import com.softserve.edu.bookinglite.exception.BookingNotFoundException;
import com.softserve.edu.bookinglite.exception.BookingExistingException;
import com.softserve.edu.bookinglite.exception.BookingInvalidDataException;
import com.softserve.edu.bookinglite.repository.ApartmentRepository;
import com.softserve.edu.bookinglite.repository.BookingRepository;
import com.softserve.edu.bookinglite.repository.BookingStatusRepository;
import com.softserve.edu.bookinglite.service.dto.BookingDto;
import com.softserve.edu.bookinglite.service.dto.CreateBookingDto;
import com.softserve.edu.bookinglite.service.mapper.BookingMapper;
import com.softserve.edu.bookinglite.util.DateUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Service
public class BookingService {

	private final String RESERVED = "Reserved";
	private final String CANCELED = "Canceled";
	private final int HOUR_CHECK_IN= 17;
	private final int HOUR_CHECK_OUT= 15;
	private final int MINUTE_CHECK_IN_AND_CHECK_OUT= 0;


	private final BookingRepository bookingRepository;
	private final BookingStatusRepository bookingStatusRepository;
	private final ApartmentRepository apartmentRepository;

	@Autowired
	public BookingService(BookingRepository bookingRepository,
						  BookingStatusRepository bookingStatusRepository,
						  ApartmentRepository apartmentRepository
	) {
		this.bookingRepository = bookingRepository;
		this.bookingStatusRepository = bookingStatusRepository;
		this.apartmentRepository = apartmentRepository;
	}


	@Transactional
	public BookingDto findBookinDTOById(Long idUser,Long bookingId) { 
		Booking booking = bookingRepository.findBookingById(idUser, bookingId); 
		return BookingMapper.instance.bookingToBaseBookingDto(booking);
	}
	
	@Transactional
	public List<BookingDto> findAllBookingsDtoByUserId(Long userId) {
		List<BookingDto> listBookingDto = new ArrayList<>();
		List<Booking> listBooking = bookingRepository.getAllByUserIdOrderByCheckInAsc(userId);
		if ( !listBooking.isEmpty()) {
			for (Booking booking : listBooking	) {
				BookingDto bookingDto = BookingMapper.instance.bookingToBaseBookingDto(booking);
				listBookingDto.add(bookingDto);				
			}
		}
		return listBookingDto;
	}
	

	@Transactional
	public List<BookingDto> findPageAllBookingsDtoByUserId(Long userId, int page, int size) {
		List<BookingDto> listBookingDto = new ArrayList<>();
		Page<Booking> pageBooking = bookingRepository.getPageAllByUserIdOrderByCheckInAsc(userId, PageRequest.of(page, size));
			for(Booking booking : pageBooking.getContent()) {
				BookingDto propertyDto = BookingMapper.instance
						.bookingToBaseBookingDto(booking);
				listBookingDto.add(propertyDto);
			}	
		return listBookingDto;
    }
	
	@Transactional
	public List<BookingDto> getAllBookingsDtoByOwnerId(Long idUserOwner) {
    	List<BookingDto> listBookingDto = new ArrayList<>();
		List<Booking> listBooking = bookingRepository.getAllBookingsByOwnerId(idUserOwner);
		if (!listBooking.isEmpty()) {
			for (Booking booking : listBooking	) {
				BookingDto bookingDto = BookingMapper.instance.bookingToBaseBookingDto(booking);
				listBookingDto.add(bookingDto);				
			}
		}
		return listBookingDto;
	}
	
	@Transactional
	public List<BookingDto> getPageAllBookingsDtoByOwnerId(Long ownerId, int page, int size) {
		List<BookingDto> listBookingDto = new ArrayList<>();
		Page<Booking> pageBooking = bookingRepository.getPageAllBookingsByOwnerId(ownerId, PageRequest.of(page, size));
		if(pageBooking.getNumberOfElements()!=0) {
			for(Booking booking : pageBooking.getContent()) {
				BookingDto propertyDto = BookingMapper.instance.bookingToBaseBookingDto(booking);
				listBookingDto.add(propertyDto);
			}
		}
		return listBookingDto;
    }
	
//if booking already exist it will return true
	@Transactional
	public boolean checkBookingIfExistByChekInandCheckOut(Long apartmentId, Date checkIn, Date checkOut) {
		if(bookingRepository.getBookingByCheck(apartmentId,DateUtil.setHourAndMinToDate(checkIn,HOUR_CHECK_IN,MINUTE_CHECK_IN_AND_CHECK_OUT),
				DateUtil.setHourAndMinToDate(checkOut,HOUR_CHECK_OUT,MINUTE_CHECK_IN_AND_CHECK_OUT))==null &&
				bookingRepository.checkBookingsExistsByDateInAndDateOut(apartmentId,DateUtil.setHourAndMinToDate(checkIn,HOUR_CHECK_IN,MINUTE_CHECK_IN_AND_CHECK_OUT),
						DateUtil.setHourAndMinToDate(checkOut,HOUR_CHECK_OUT,MINUTE_CHECK_IN_AND_CHECK_OUT))==null
				){
		    return false;
        }
        return true;
	}

	@Transactional
	public boolean createBooking(CreateBookingDto createBookingDto, Long userId, Long apartmentId) 
			throws ApartmentNotFoundException, BookingInvalidDataException, BookingExistingException {
		Apartment apartment= apartmentRepository.findById(apartmentId)
				.orElseThrow(() -> new ApartmentNotFoundException(apartmentId));
		
		if(checkValidationDate (createBookingDto)==false || 
				createBookingDto.getNumberOfGuests() > apartment.getNumberOfGuests()){
			throw new BookingInvalidDataException();
  		}
		
		if (checkBookingIfExistByChekInandCheckOut(apartmentId,createBookingDto.getCheckIn(),createBookingDto.getCheckOut())==false) {
            Booking booking = new Booking();
            booking.setApartment(apartment);
            User user = new User();
            user.setId(userId);
            booking.setUser(user); 
            booking.setCheckIn(DateUtil.setHourAndMinToDate(createBookingDto.getCheckIn(),HOUR_CHECK_IN,MINUTE_CHECK_IN_AND_CHECK_OUT));
            booking.setCheckOut(DateUtil.setHourAndMinToDate(createBookingDto.getCheckOut(),HOUR_CHECK_OUT,MINUTE_CHECK_IN_AND_CHECK_OUT));
            booking.setTotalPrice(getPriceForPeriod(apartment.getPrice(),
            		createBookingDto.getCheckIn(),createBookingDto.getCheckOut()));
            booking.setBookingStatus(bookingStatusRepository.findByName(RESERVED));
            Booking result = bookingRepository.save(booking); 
            if (result != null){
                return true;
            }
            else return false;
  		}
  		else {
            throw new BookingExistingException();
        }
	}
	
	@Transactional
    public boolean cancelBooking(Long userId, Long bookingId) throws BookingNotFoundException, BookingCancelException {
		Booking booking = bookingRepository.findBookingById(userId, bookingId);
		if(booking==null){
			throw new BookingNotFoundException(bookingId); 
		}
		if(!booking.getBookingStatus().getName().equals(RESERVED)){
			throw new BookingCancelException(booking.getBookingStatus().getName()); 
		}
		if( booking.getCheckIn().after(new Date())   ) {
			booking.setBookingStatus(bookingStatusRepository.findByName(CANCELED));
			return true;   
		}
		else if(booking.getCheckIn().compareTo(new Date())==0
				&& booking.getCheckIn().before(DateUtil.setHourAndMinToDate(new Date(),HOUR_CHECK_IN,MINUTE_CHECK_IN_AND_CHECK_OUT))) {
			booking.setBookingStatus(bookingStatusRepository.findByName(CANCELED));
			return true;  
		}
		else return false;        		  	       	 	  	    	    		
    }
    
    private boolean checkValidationDate (CreateBookingDto bookingDto){
    	boolean isValid= true;
    	
    	if(bookingDto.getCheckOut().before(bookingDto.getCheckIn())) {
    		isValid= false;
    	}
    	if(bookingDto.getCheckIn().before(new Date()) ) {
    		isValid= false;
    	}
    	if(bookingDto.getCheckOut().compareTo(bookingDto.getCheckIn())==0) {
    		isValid= false;
    	}  	
    	return isValid;
    }    

    public BigDecimal getPriceForPeriod(BigDecimal priceOneDay, Date checkIn, Date checkOut) {
    	BigDecimal priceForPeriod= new BigDecimal(BigInteger.ZERO,2);
    	int diff= DateUtil.countDay(checkIn, checkOut); 
    	priceOneDay= priceOneDay.multiply( new BigDecimal(diff));
    	return priceForPeriod.add(priceOneDay);
    }
    public Date setHourAndMinToDate(Date date,int hour,int minuts){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE,minuts);
		return calendar.getTime();
	}
}
