package com.softserve.edu.bookinglite.service;


import com.softserve.edu.bookinglite.entity.Apartment;
import com.softserve.edu.bookinglite.entity.Booking;
import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.exception.*;
import com.softserve.edu.bookinglite.repository.ApartmentRepository;
import com.softserve.edu.bookinglite.repository.BookingRepository;
import com.softserve.edu.bookinglite.repository.BookingStatusRepository;
import com.softserve.edu.bookinglite.service.dto.BookingDto;
import com.softserve.edu.bookinglite.service.dto.CreateBookingDto;
import com.softserve.edu.bookinglite.service.mapper.BookingMapper;
import com.softserve.edu.bookinglite.util.BookingUtil;
import com.softserve.edu.bookinglite.util.DateUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BookingService {

	private final String RESERVED = "Reserved";
	private final String CANCELED = "Canceled";
	private final int HOUR_CHECK_IN= 17;
	private final int HOUR_CHECK_OUT= 15;
    private final String ACTUAL_BOOKINGS = "actualBookings";
    private final String ARCHIEVE_BOOKINGS = "archieveBookings";


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
	public BookingDto findBookinDTOById(Long idUser,Long bookingId)  throws BookingNotFoundException{ 
		Booking booking = bookingRepository.findBookingById(idUser, bookingId); 
		if(booking==null) throw new BookingNotFoundException(bookingId);
		return BookingMapper.instance.bookingToBaseBookingDto(booking);
	}

	@Transactional
	public Page<BookingDto> findPageAllBookingsDtoByUserId(Long userId, int page, int size, String filterBookingsByDates) {
		Page <BookingDto> pageBookings = null;
		Date nowFullDate = new Date();
		Date nowShortDate =  DateUtils.setHourAndMinToDate(
				new Date(nowFullDate.getYear(), nowFullDate.getMonth(), nowFullDate.getDay()), nowFullDate.getHours());
		if (ACTUAL_BOOKINGS.equals(filterBookingsByDates)) {			
			pageBookings= BookingMapper.instance.toPageBookingDto(
					bookingRepository.getPageActualBookingsByUserId(userId, nowShortDate, PageRequest.of(page, size)));
		} 
		else if(ARCHIEVE_BOOKINGS.equals(filterBookingsByDates)) {
			pageBookings= BookingMapper.instance.toPageBookingDto(
					bookingRepository.getPageArchieveBookingsByUserId(userId, nowShortDate, PageRequest.of(page, size)));
		} else {
			pageBookings= BookingMapper.instance.toPageBookingDto(
					bookingRepository.getPageAllBookingsByUserId(userId, PageRequest.of(page, size) ));
		}	
		return pageBookings;
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
				booking.getCheckIn().before(DateUtils.setHourAndMinToDate
						(new Date(),HOUR_CHECK_IN))) ||
				booking.getCheckIn().after(new Date())) {
			booking.setBookingStatus(bookingStatusRepository.findByName(CANCELED));
			bookingRepository.save(booking);
			return true;
		}
		else throw new BookingCancelException();
	}

	@Transactional
	public List<BookingDto> getAllBookingsDtoByOwnerId(Long idUserOwner) {
    	List<BookingDto> listBookingDto = new ArrayList<>();
    	bookingRepository.getAllBookingsByOwnerId(idUserOwner).forEach(
				b -> listBookingDto.add( BookingMapper.instance.bookingToBaseBookingDto(b)));
		return listBookingDto;
	}

	@Transactional
	public Page<BookingDto> getPageAllBookingsDtoByOwnerId(Long ownerId, int page, int size) {
		return BookingMapper.instance.toPageBookingDto(
				bookingRepository.getPageAllBookingsByOwnerId(ownerId, PageRequest.of(page, size)));
    }

	@Transactional
	public boolean createBookingWithValidation(CreateBookingDto createBookingDto, Long userId, Long apartmentId)
			throws ApartmentNotFoundException, BookingExistingException, BookingInvalidDataException, NumberOfGuestsException {
		Apartment apartment= validateApartmentAndDate(apartmentId,createBookingDto.getCheckIn(), createBookingDto.getCheckOut());
		if (createBookingDto.getNumberOfGuests() <= apartment.getNumberOfGuests()
		&& validateApartmentAvailable(apartmentId,createBookingDto.getCheckIn(),createBookingDto.getCheckOut())) {
			saveBooking(userId,apartment,createBookingDto);
			return true;
		}throw new NumberOfGuestsException() ;
	}
	@Transactional
	public Apartment validateApartmentAndDate(Long apartmentId,Date in,Date out) throws ApartmentNotFoundException, BookingInvalidDataException {
		Apartment apartment= apartmentRepository.findById(apartmentId)
				.orElseThrow(() -> new ApartmentNotFoundException(apartmentId));
		if(!DateUtils.checkValidationDate(in, out)){
			throw new BookingInvalidDataException();
		}
		return apartment;
	}
	@Transactional
	public boolean validateApartmentAvailable(Long apartmentId, Date in, Date out) throws BookingExistingException {
	if(!bookingRepository.getBookingByCheck(apartmentId,
			DateUtils.setHourAndMinToDate(in, HOUR_CHECK_IN),
			DateUtils.setHourAndMinToDate(out, HOUR_CHECK_OUT))){
		return true;
	}else throw new BookingExistingException();
	}
    @Transactional
	void saveBooking(Long userId, Apartment apartment, CreateBookingDto createBookingDto){
	Booking booking = new Booking();
	booking.setApartment(apartment);
	User user = new User();
	user.setId(userId);
	booking.setUser(user);
	booking.setCheckIn(DateUtils.setHourAndMinToDate(createBookingDto.getCheckIn(),HOUR_CHECK_IN));
	booking.setCheckOut(DateUtils.setHourAndMinToDate(createBookingDto.getCheckOut(),HOUR_CHECK_OUT));
	booking.setTotalPrice(BookingUtil.getPriceForPeriod(apartment.getPrice(),
			createBookingDto.getCheckIn(),createBookingDto.getCheckOut()));
	booking.setBookingStatus(bookingStatusRepository.findByName(RESERVED));
	bookingRepository.save(booking);
}

}