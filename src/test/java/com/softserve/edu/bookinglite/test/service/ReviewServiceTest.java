package com.softserve.edu.bookinglite.test.service;


import com.softserve.edu.bookinglite.entity.*;
import com.softserve.edu.bookinglite.exception.*;
import com.softserve.edu.bookinglite.repository.BookingRepository;
import com.softserve.edu.bookinglite.repository.PropertyRepository;
import com.softserve.edu.bookinglite.repository.ReviewRepository;
import com.softserve.edu.bookinglite.service.ReviewService;
import com.softserve.edu.bookinglite.service.dto.CreateReviewDto;
import com.softserve.edu.bookinglite.service.dto.ReviewDto;
import com.softserve.edu.bookinglite.service.mapper.ReviewMapper;
import com.softserve.edu.bookinglite.util.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ReviewServiceTest {

    @InjectMocks
    ReviewService reviewService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private PropertyRepository propertyRepository;

    private final String MESSAGE = "Test message";
    private final int INDEX = 0;
    private final Long ID = 1L;
    private final Long BAD_ID = 10L;
    private final String CANCELED = "Canceled";
    private final String RESERVED = "Reserved";
    private final Date VALID_DATE = DateUtils.setHourAndMinToDate(new Date(116, 11, 8), 12);
    private final Date INVALID_DATE = DateUtils.setHourAndMinToDate(new Date(119, 11, 8), 12);

    @Test
    public void findReviewByBooking() throws ReviewNotFoundExeption {
        Booking booking = initBooking(ID, VALID_DATE);
        Mockito.when(reviewRepository.findByBookingId(ID)).thenReturn(booking.getReview());
        assertThat(reviewService.findReviewByBookingId(ID))
                .isEqualTo(ReviewMapper.instance.reviewToBaseReviewDto(booking.getReview()));
    }

    @Test(expected = ReviewNotFoundExeption.class)
    public void findReviewByBookingIdNotFoundException() throws ReviewNotFoundExeption {
        Mockito.when(reviewRepository.findByBookingId(ID)).thenReturn(null);
        assertThat(reviewService.findReviewByBookingId(ID));
    }

    @Test(expected = PropertyNotFoundException.class)
    public void findAllReviewsByPropertyIdPropertyNotFoundExceptionTest() throws PropertyNotFoundException {
        Mockito.when(propertyRepository.findById(ID)).thenReturn(Optional.empty());
        assertThat(reviewService.findAllReviewsByPropertyId(ID));
    }

    @Test
    public void findAllReviewsByPropertyIdTest() throws PropertyNotFoundException {
        List<Review> list = new ArrayList<>();
        Booking booking = initBooking(ID, VALID_DATE);
        Review review = booking.getReview();
        list.add(review);
        Mockito.when(propertyRepository.findById(ID)).thenReturn(Optional.of(booking.getApartment().getProperty()));
        Mockito.when(reviewRepository.findAllReviewsByIdProperty(ID)).thenReturn(list);
        assertThat(reviewService.findAllReviewsByPropertyId(ID).get(INDEX))
                .isEqualTo(ReviewMapper.instance.reviewToBaseReviewDto(review));
    }

    @Test(expected = BookingNotFoundException.class)
    public void addReviewBookingNotFoundExceptionTest() throws BookingNotFoundException, ReviewOwnerException, CantLeaveReviewException {
        Mockito.when(bookingRepository.findById(ID)).thenReturn(Optional.empty());
        CreateReviewDto reviewDto = new CreateReviewDto();
       reviewService.addReview(reviewDto, ID, ID);
    }

    @Test(expected = ReviewOwnerException.class)
    public void addReviewBookingReviewOwnerExceptionTest() throws BookingNotFoundException, ReviewOwnerException, CantLeaveReviewException {
        Booking booking = initBooking(BAD_ID, VALID_DATE);
        Mockito.when(bookingRepository.findById(ID)).thenReturn(Optional.of(booking));
        CreateReviewDto reviewDto = new CreateReviewDto();
       reviewService.addReview(reviewDto, ID, ID);
    }

    @Test(expected = CantLeaveReviewException.class)
    public void addReviewBookingCantLeaveReviewExceptionTest() throws BookingNotFoundException, ReviewOwnerException, CantLeaveReviewException {
        Booking booking = initBooking(ID, INVALID_DATE);
        Mockito.when(bookingRepository.findById(ID)).thenReturn(Optional.of(booking));
        CreateReviewDto reviewDto = new CreateReviewDto();
        reviewService.addReview(reviewDto, ID, ID);
    }

    @Test(expected = CantLeaveReviewException.class)
    public void addReviewBookingCantLeaveReviewExceptionWhenBookingStatusIsCanceledTest() throws BookingNotFoundException, ReviewOwnerException, CantLeaveReviewException {
        Booking booking = initBooking(ID, VALID_DATE);
        Mockito.when(bookingRepository.findById(ID)).thenReturn(Optional.of(booking));
        CreateReviewDto reviewDto = new CreateReviewDto();
        reviewService.addReview(reviewDto, ID, ID);
    }

    public Booking initBooking(Long id_User, Date date) {
        Booking booking = new Booking();
        User user = new User();
        Apartment apartment = new Apartment();
        Property property = new Property();
        BookingStatus bookingStatus = new BookingStatus();
        property.setId(ID);
        user.setId(id_User);
        //apartment
        apartment.setId(ID);
        apartment.setProperty(property);
        //review
        Review review = new Review();
        review.setId(ID);
        review.setMessage(MESSAGE);
        review.setRating(5f);
        review.setBooking(booking);
        //booking
        booking.setId(ID);
        bookingStatus.setId(ID);
        booking.setApartment(apartment);
        booking.setUser(user);
        bookingStatus.setName(CANCELED);
        booking.setBookingStatus(bookingStatus);
        booking.setReview(review);
        booking.setCheckOut(DateUtils.setHourAndMinToDate(date, 12));
        return booking;
    }

}
