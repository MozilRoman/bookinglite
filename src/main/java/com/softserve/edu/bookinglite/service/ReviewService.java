package com.softserve.edu.bookinglite.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import com.softserve.edu.bookinglite.service.dto.CreateReviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softserve.edu.bookinglite.entity.Booking;
import com.softserve.edu.bookinglite.entity.Property;
import com.softserve.edu.bookinglite.entity.Review;
import com.softserve.edu.bookinglite.exception.*;
import com.softserve.edu.bookinglite.repository.BookingRepository;
import com.softserve.edu.bookinglite.repository.PropertyRepository;
import com.softserve.edu.bookinglite.repository.ReviewRepository;
import com.softserve.edu.bookinglite.service.dto.ReviewDto;
import com.softserve.edu.bookinglite.service.mapper.ReviewMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Service
public class ReviewService {

    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;
    private final PropertyRepository propertyRepository;

    private final String CANCELED = "Canceled";

    @Autowired
    public ReviewService(BookingRepository bookingRepository, ReviewRepository reviewRepository, PropertyRepository propertyRepository) {
        this.bookingRepository = bookingRepository;
        this.reviewRepository = reviewRepository;
        this.propertyRepository = propertyRepository;
    }

    public ReviewDto findReviewByBookingId(Long bookingId) throws ReviewNotFoundExeption {
        Review review = reviewRepository.findByBookingId(bookingId);
        if (review == null) {
            throw new ReviewNotFoundExeption();
        }
            return ReviewMapper.instance.reviewToBaseReviewDto(review);
    }

    public Page<ReviewDto> findPageAllReviewsByPropertyId(Long propertyId, int page, int size) {
        Page<ReviewDto> listReviewsDto = ReviewMapper.instance.toPageReviewDto
                (reviewRepository.findPageAllReviewsByIdProperty(propertyId, PageRequest.of(page, size)));
        return listReviewsDto;
    }

    @Transactional
    public int findCountReviewsByPropertyId(Long propertyId) throws PropertyNotFoundException {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));
        return reviewRepository.countAllReviewsByIdProperty(property.getId());
    }

    public boolean addReview(CreateReviewDto reviewDto, Long bookingId, Long userId)
            throws BookingNotFoundException, ReviewOwnerException, CantLeaveReviewException, ReviewAlreadyExistExeption {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
        Property property = booking.getApartment().getProperty();
        if (validBookingUserEqualsReviewUser(booking, userId) && validBookingStatusIsNotCanceledAndCheckOutDateNotBeforeNowDate(booking)) {
            validAndSaveReview(booking, reviewDto);
            updatePropertyRanking(property, getAvgFromRankingProperty(property.getId()));
            return true;
        }
        return false;

    }

    public float getAvgFromRankingProperty(Long propertyId) {
        return reviewRepository.findAvgRankingByIdProperty(propertyId);
    }

    private void updatePropertyRanking(Property property, float ranking) {
        property.setRating(ranking);
        propertyRepository.saveAndFlush(property);
    }

    private boolean validBookingUserEqualsReviewUser(Booking booking, Long userId) throws ReviewOwnerException {
        if (booking.getUser().getId().equals(userId)) {
            return true;
        } else {
            throw new ReviewOwnerException();
        }
    }

    private boolean validBookingStatusIsNotCanceledAndCheckOutDateNotBeforeNowDate(Booking booking) throws CantLeaveReviewException {
        if (booking.getCheckOut().before(new Date()) && !booking.getBookingStatus().getName().equals(CANCELED)) {
            return true;
        } else {
            throw new CantLeaveReviewException();
        }
    }

    private void validAndSaveReview(Booking booking, CreateReviewDto reviewDto) throws ReviewAlreadyExistExeption {
        if (booking.getReview() != null) {
            throw new ReviewAlreadyExistExeption();
        }
        Review review = new Review();
        review.setMessage(reviewDto.getMessage());
        review.setRating(reviewDto.getRating());
        review.setBooking(booking);
        booking.setReview(review);
        reviewRepository.saveAndFlush(review);
    }
}
