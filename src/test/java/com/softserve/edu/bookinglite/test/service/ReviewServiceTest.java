package com.softserve.edu.bookinglite.test.service;

import com.softserve.edu.bookinglite.entity.Booking;
import com.softserve.edu.bookinglite.entity.Review;
import com.softserve.edu.bookinglite.exception.ReviewNotFoundExeption;
import com.softserve.edu.bookinglite.repository.BookingRepository;
import com.softserve.edu.bookinglite.repository.ReviewRepository;
import com.softserve.edu.bookinglite.service.ReviewService;
import com.softserve.edu.bookinglite.service.mapper.ReviewMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReviewServiceTest {

    @Autowired
    ReviewService reviewService;
    @MockBean
    private BookingRepository bookingRepository;
    @MockBean
    private ReviewRepository reviewRepository;

    private final String MESSAGE = "Test message";
    private final int INDEX = 0;
    private final Long ID = 1L;

   // @Test(expected = ReviewNotFoundExeption.class)
    @Test
    public void findReviewByBooking() throws ReviewNotFoundExeption {
        Review review=new Review();
        Booking booking=new Booking();
        booking.setId(ID);
        review.setId(ID);
        review.setMessage(MESSAGE);
        review.setRating(5f);
        review.setBooking(booking);
        Mockito.when(reviewRepository.findByBookingId(ID)).thenReturn(review);
        assertThat(reviewService.findReviewByBookingId(ID)).isEqualTo(ReviewMapper.instance.toDto(review));
    }
    @Test(expected = ReviewNotFoundExeption.class)
    public void findReviewByBookingIdNotFoundException() throws ReviewNotFoundExeption {
        Mockito.when(reviewRepository.findByBookingId(ID)).thenReturn(null);
        assertThat(reviewService.findReviewByBookingId(ID));
    }

}
