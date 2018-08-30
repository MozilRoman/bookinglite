package com.softserve.edu.bookinglite.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.softserve.edu.bookinglite.service.dto.BookingDto;
import com.softserve.edu.bookinglite.service.dto.ReviewDto;
import com.softserve.edu.bookinglite.service.mapper.BookingMapper;
import com.softserve.edu.bookinglite.service.mapper.ReviewMapper;

@Service
public class ReviewService {

    private final BookingRepository bookingRepository;
    private  final ReviewRepository reviewRepository;
    private final PropertyRepository propertyRepository;

    private final String CANCELED = "Canceled";
    @Autowired
    public ReviewService(BookingRepository bookingRepository, ReviewRepository reviewRepository, PropertyRepository propertyRepository) {
        this.bookingRepository = bookingRepository;
        this.reviewRepository = reviewRepository;
        this.propertyRepository = propertyRepository;
    }

    public ReviewDto findReviewByBookingId(Long bookingId) throws ReviewNotFoundExeption {
        Review review =reviewRepository.findByBookingId(bookingId);
        if(review==null){
            throw new ReviewNotFoundExeption();
        }
            return ReviewMapper.instance.reviewToBaseReviewDto(review);
    }

    public List<ReviewDto> findAllReviewsByPropertyId(Long propertyId) throws PropertyNotFoundException {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));
        List<ReviewDto> reviewDtos = new ArrayList<>();
        for (Review review : reviewRepository.findAllReviewsByIdProperty(property.getId())) {
            ReviewDto reviewDto = ReviewMapper.instance.reviewToBaseReviewDto(review);
            reviewDtos.add(reviewDto);
        }
        return reviewDtos;
    }
    
    @Transactional
    public int findCountReviewsByPropertyId(Long propertyId) throws PropertyNotFoundException {
    	 Property property = propertyRepository.findById(propertyId)
                 .orElseThrow(() -> new PropertyNotFoundException(propertyId));
    	return reviewRepository.findAllReviewsByIdProperty(property.getId()).size();
    }

    public boolean addReview(CreateReviewDto reviewDto, Long bookingId, Long userId)
            throws BookingNotFoundException, ReviewOwnerException, CantLeaveReviewException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(()-> new BookingNotFoundException(bookingId));
        BookingDto bookingDto = BookingMapper.instance.bookingToBaseBookingDto(booking);
        Property property = booking.getApartment().getProperty();
        if (booking.getUser().getId().equals(userId)){
            if (bookingDto.getCheckOut().before(new Date()) && !bookingDto.getBookingStatus().getName().equals(CANCELED)) {
            Review review = new Review();
                review.setMessage(reviewDto.getMessage());
                review.setRating(reviewDto.getRating());
                review.setBooking(booking);
                reviewRepository.saveAndFlush(review);
                booking.setReview(review);
                bookingRepository.saveAndFlush(booking);
                if (property.getRating() != null) {
                    List<Review> reviews = reviewRepository.findAllReviewsByIdProperty(property.getId());
                    Float sum = 0.0f;
                    for (Review r : reviews) {
                        sum += r.getRating();
                    }
                    Float newRating = sum / reviews.size();
                    property.setRating(newRating);
                }
                propertyRepository.save(property);
                return true;
            }else {
                throw new CantLeaveReviewException();
            }
        }else {
            throw new ReviewOwnerException();
        }
    }

}
