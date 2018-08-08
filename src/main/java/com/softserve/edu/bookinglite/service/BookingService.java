package com.softserve.edu.bookinglite.service;


import com.softserve.edu.bookinglite.entity.Apartment;
import com.softserve.edu.bookinglite.entity.Booking;
import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.repository.BookingRepository;
import com.softserve.edu.bookinglite.repository.BookingStatusRepository;
import com.softserve.edu.bookinglite.service.dto.BookingDto;
import com.softserve.edu.bookinglite.service.dto.CreateBookingDto;
import com.softserve.edu.bookinglite.service.mapper.BookingMapper;
import com.softserve.edu.bookinglite.util.DateUtil;

import org.springframework.beans.factory.annotation.Autowired;
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
	public BookingDto findBookinDTOById(Long id) { 
		Optional<Booking> booking = bookingRepository.findById(id);
		return booking.map(BookingMapper.instance::bookingToBaseBookingDto).orElse(null);
	}
	
	@Transactional//TODO spring pageable
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
	public boolean createBooking(CreateBookingDto createBookingDto, Long userId, Long apartmentId) {
		if(checkValidationDate (createBookingDto)==false || 
				createBookingDto.getNumberOfGuests() > apartmentService.findApartmentDtoById(apartmentId).getNumberOfGuests()){
			return false;
  		}
		if (checkBookingIfExistByChekInandCheckOut(apartmentId,createBookingDto.getCheckIn(),createBookingDto.getCheckOut())==false) {
            Booking booking = new Booking();
            if (apartmentService.findApartmentDtoById(apartmentId) != null) {
                Apartment apartment = new Apartment();
                apartment.setId(apartmentId);
                booking.setApartment(apartment);
            }
            if (userService.findById(userId) != null) {
                User user = new User();
                user.setId(userId);
                booking.setUser(user); 
                }
            booking.setCheckIn(DateUtil.setHourAndMinToDate(createBookingDto.getCheckIn(),HOUR_CHECK_IN,MINUTE_CHECK_IN_AND_CHECK_OUT));
            booking.setCheckOut(DateUtil.setHourAndMinToDate(createBookingDto.getCheckOut(),HOUR_CHECK_OUT,MINUTE_CHECK_IN_AND_CHECK_OUT));
            booking.setTotalPrice(getPriceForPeriod(apartmentService.findApartmentDtoById(apartmentId).getPrice(),
            		createBookingDto.getCheckIn(),createBookingDto.getCheckOut()));
            booking.setBookingStatus(bookingStatusRepository.findByName(RESERVED));
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
    @Transactional
	public List<BookingDto> getAllBookingsDtoByOwnerId(Long idUserOwner){
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
}

