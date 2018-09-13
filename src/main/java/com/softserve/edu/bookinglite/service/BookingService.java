package com.softserve.edu.bookinglite.service;


import com.softserve.edu.bookinglite.entity.Apartment;
import com.softserve.edu.bookinglite.entity.Booking;
import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.exception.*;
import com.softserve.edu.bookinglite.repository.ApartmentRepository;
import com.softserve.edu.bookinglite.repository.BookingRepository;
import com.softserve.edu.bookinglite.repository.BookingStatusRepository;
import com.softserve.edu.bookinglite.repository.PropertyRepository;
import com.softserve.edu.bookinglite.service.dto.BookingDto;
import com.softserve.edu.bookinglite.service.dto.CreateBookingDto;
import com.softserve.edu.bookinglite.service.mapper.BookingMapper;
import com.softserve.edu.bookinglite.util.BookingUtil;
import com.softserve.edu.bookinglite.util.DateUtils;
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
    private final int HOUR_CHECK_IN = 17;
    private final int HOUR_CHECK_OUT = 15;
    private final String ACTUAL_BOOKINGS = "actualBookings";
    private final String FUTURE_BOOKINGS = "futureBookings";
    private final String PAST_BOOKINGS = "pastBookings";
    private final String ARCHIEVE_BOOKINGS = "archieveBookings";
    private final BookingRepository bookingRepository;
    private final BookingStatusRepository bookingStatusRepository;
    private final ApartmentRepository apartmentRepository;
    private final PropertyRepository propertyRepository;

	
	@Autowired
	   public BookingService(BookingRepository bookingRepository,
	                         BookingStatusRepository bookingStatusRepository,
	                         ApartmentRepository apartmentRepository,
	                         PropertyRepository propertyRepository
	   ) {
	       this.bookingRepository = bookingRepository;
	       this.bookingStatusRepository = bookingStatusRepository;
	       this.apartmentRepository = apartmentRepository;
	       this.propertyRepository = propertyRepository;
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
    public boolean createBookingWithValidation(CreateBookingDto createBookingDto, Long userId, Long apartmentId)
            throws ApartmentNotFoundException, BookingExistingException, BookingInvalidDataException, NumberOfGuestsException {
        Apartment apartment = validateApartmentExistAndDateIsNotInvalid(apartmentId, createBookingDto.getCheckIn(), createBookingDto.getCheckOut());
        if (validateBookingGuestsArrivalsToApartmentGuestArrivals(createBookingDto.getNumberOfGuests(), apartment.getNumberOfGuests())
                && isApartmentAvailable(apartmentId, createBookingDto.getCheckIn(), createBookingDto.getCheckOut())) {
            saveBooking(userId, apartment, createBookingDto);
            return true;
        }
        return false;
    }

    public boolean validateBookingGuestsArrivalsToApartmentGuestArrivals(int booking_guest, int apartment_guests) throws NumberOfGuestsException {
        if (apartment_guests < booking_guest) {
            throw new NumberOfGuestsException();
        }
        return true;
    }

    @Transactional
    public Apartment validateApartmentExistAndDateIsNotInvalid(Long apartmentId, Date in, Date out) throws ApartmentNotFoundException, BookingInvalidDataException {
        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new ApartmentNotFoundException(apartmentId));
        if (!DateUtils.checkValidationDate(in, out)) {
            throw new BookingInvalidDataException();
        }
        return apartment;
    }

    @Transactional
    public boolean isApartmentAvailable(Long apartmentId, Date in, Date out) throws BookingExistingException {
        if (!bookingRepository.isApartmentBookedWithinDateRange(apartmentId,
                DateUtils.setHourAndMinToDate(in, HOUR_CHECK_IN),
                DateUtils.setHourAndMinToDate(out, HOUR_CHECK_OUT))) {
            return true;
        } else throw new BookingExistingException();
    }


    private void saveBooking(Long userId, Apartment apartment, CreateBookingDto createBookingDto) {
        Booking booking = new Booking();
        booking.setApartment(apartment);
        User user = new User();
        user.setId(userId);
        booking.setUser(user);
        booking.setCheckIn(DateUtils.setHourAndMinToDate(createBookingDto.getCheckIn(), HOUR_CHECK_IN));
        booking.setCheckOut(DateUtils.setHourAndMinToDate(createBookingDto.getCheckOut(), HOUR_CHECK_OUT));
        booking.setTotalPrice(BookingUtil.getPriceForPeriod(apartment.getPrice(),
                createBookingDto.getCheckIn(), createBookingDto.getCheckOut()));
        booking.setBookingStatus(bookingStatusRepository.findByName(RESERVED));
        bookingRepository.save(booking);
    }

    @Transactional
    public Page<BookingDto> getPageBookingsByOwner(Long idProperty, Long idUserOwner, String filterBooking, int page, int size) {
        Date date = new Date();
        Page<BookingDto> pageBookings = null;
        switch (filterBooking) {
            case (ACTUAL_BOOKINGS):
                pageBookings = BookingMapper.instance.toPageBookingDto(
                        bookingRepository.getActiveBookingsByPropertyAndOwnerId(idProperty, idUserOwner, date, PageRequest.of(page, size)));
                break;
            case (PAST_BOOKINGS):
                pageBookings = BookingMapper.instance.toPageBookingDto(
                        bookingRepository.getPastBookingsByPropertyAndOwnerId(idProperty, idUserOwner, date, PageRequest.of(page, size)));
                break;
            case (FUTURE_BOOKINGS):
                pageBookings = BookingMapper.instance.toPageBookingDto(
                        bookingRepository.getFutureBookingsByPropertyAndOwnerId(idProperty, idUserOwner, date, PageRequest.of(page, size)));
                break;
        }
        return pageBookings;
    }

}