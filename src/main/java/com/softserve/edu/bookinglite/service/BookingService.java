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
import com.softserve.edu.bookinglite.util.BookingUtil;
import com.softserve.edu.bookinglite.util.DateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private Logger logger = LoggerFactory.getLogger(BookingService.class);
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

	@Transactional
	public boolean createBooking(CreateBookingDto createBookingDto, Long userId, Long apartmentId)
			throws ApartmentNotFoundException, BookingExistingException, BookingInvalidDataException {
		Apartment apartment= apartmentRepository.findById(apartmentId)
				.orElseThrow(() -> new ApartmentNotFoundException(apartmentId));
		if(DateUtil.checkValidationDate(createBookingDto.getCheckIn(),createBookingDto.getCheckOut())==false){
			throw new BookingInvalidDataException();
		}
		if (createBookingDto.getNumberOfGuests() <= apartment.getNumberOfGuests()
		        && bookingRepository.getBookingByCheck(apartmentId,
				DateUtil.setHourAndMinToDate(createBookingDto.getCheckIn(),HOUR_CHECK_IN,MINUTE_CHECK_IN_AND_CHECK_OUT),
				DateUtil.setHourAndMinToDate(createBookingDto.getCheckOut(),HOUR_CHECK_OUT,MINUTE_CHECK_IN_AND_CHECK_OUT))==false) {
            Booking booking = new Booking();
            booking.setApartment(apartment);
            User user = new User();
            user.setId(userId);
            booking.setUser(user); 
            booking.setCheckIn(DateUtil.setHourAndMinToDate(createBookingDto.getCheckIn(),HOUR_CHECK_IN,MINUTE_CHECK_IN_AND_CHECK_OUT));
            booking.setCheckOut(DateUtil.setHourAndMinToDate(createBookingDto.getCheckOut(),HOUR_CHECK_OUT,MINUTE_CHECK_IN_AND_CHECK_OUT));
            booking.setTotalPrice(BookingUtil.getPriceForPeriod(apartment.getPrice(),
            		createBookingDto.getCheckIn(),createBookingDto.getCheckOut()));
            booking.setBookingStatus(bookingStatusRepository.findByName(RESERVED));
            Booking result = bookingRepository.save(booking);
            logger.info("booking was created successful "+result.getId());
                return true;
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
		if( (booking.getCheckIn().compareTo(new Date())==0 &&
				booking.getCheckIn().before(DateUtil.setHourAndMinToDate
						(new Date(),HOUR_CHECK_IN,MINUTE_CHECK_IN_AND_CHECK_OUT))) ||
				booking.getCheckIn().after(new Date())) {
			booking.setBookingStatus(bookingStatusRepository.findByName(CANCELED));
			bookingRepository.save(booking);
			return true;   
		}
		else throw new BookingCancelException();      		  	       	 	  	    	    		
    }
}