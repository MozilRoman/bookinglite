package com.softserve.edu.bookinglite.service;

import com.softserve.edu.bookinglite.entity.Booking;
import com.softserve.edu.bookinglite.entity.Property;
import com.softserve.edu.bookinglite.entity.Review;
import com.softserve.edu.bookinglite.exception.BookingNotFoundException;
import com.softserve.edu.bookinglite.exception.PropertyNotFoundException;
import com.softserve.edu.bookinglite.repository.BookingRepository;
import com.softserve.edu.bookinglite.repository.PropertyRepository;
import com.softserve.edu.bookinglite.repository.ReviewRepository;
import com.softserve.edu.bookinglite.service.dto.BookingDto;
import com.softserve.edu.bookinglite.service.dto.ReviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.softserve.edu.bookinglite.service.mapper.ReviewMapper;
import com.softserve.edu.bookinglite.service.mapper.BookingMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReviewService {

    private final BookingRepository bookingRepository;
    private  final ReviewRepository reviewRepository;
    private final PropertyRepository propertyRepository;

    @Autowired
    public ReviewService(BookingRepository bookingRepository, ReviewRepository reviewRepository, PropertyRepository propertyRepository) {
        this.bookingRepository = bookingRepository;
        this.reviewRepository = reviewRepository;
        this.propertyRepository = propertyRepository;
    }

    public List<ReviewDto> findReviewByBookingId(Long bookingId){
        List<ReviewDto> reviewDtos = new ArrayList<>();
        for (Review review :reviewRepository.findByBookingId(bookingId)) {
            ReviewDto reviewDto = ReviewMapper.instance.toDto(review);
            reviewDtos.add(reviewDto);
        }
        return reviewDtos;
    }

    public List<ReviewDto> findAllReviewsByPropertyId(Long propertyId) throws PropertyNotFoundException {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));
        List<ReviewDto> reviewDtos = new ArrayList<>();
        for (Review review : reviewRepository.findAllReviewsByIdProperty(property.getId())) {
            ReviewDto reviewDto = ReviewMapper.instance.toDto(review);
            reviewDtos.add(reviewDto);
        }
        return reviewDtos;
    }

    public boolean addReview(ReviewDto reviewDto, Long bookingId, Long userId) throws BookingNotFoundException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(()-> new BookingNotFoundException(bookingId));
        BookingDto bookingDto = BookingMapper.instance.bookingToBaseBookingDto(booking);
        Property property = booking.getApartment().getProperty();
        if (booking.getUser().getId().equals(userId) && bookingDto.getCheckOut().before(new Date())){
            Review review = new Review();
            review.setMessage(reviewDto.getMessage());
            review.setRating(reviewDto.getRating());
            review.setBooking(booking);
            reviewRepository.save(review);
            booking.setReview(review);
            bookingRepository.save(booking);

            if (property.getRating() == null){
                property.setRating(review.getRating());
            }else {
                List<Review> reviews = reviewRepository.findAllReviewsByIdProperty(property.getId());
                Float sum = 0.0f;
                int quantity = 0;
                for(Review r : reviews){
                    sum += r.getRating();
                    quantity ++;
                }
                Float newRating = sum/quantity;
                property.setRating(newRating);
            }
            propertyRepository.save(property);
            return true;
        }
        return false;
    }

}
