package com.softserve.edu.bookinglite.service;

import com.softserve.edu.bookinglite.entity.Apartment;
import com.softserve.edu.bookinglite.entity.Booking;
import com.softserve.edu.bookinglite.entity.Property;
import com.softserve.edu.bookinglite.entity.Review;
import com.softserve.edu.bookinglite.repository.BookingRepository;
import com.softserve.edu.bookinglite.repository.PropertyRepository;
import com.softserve.edu.bookinglite.repository.ReviewRepository;
import com.softserve.edu.bookinglite.service.dto.ReviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.softserve.edu.bookinglite.service.mapper.ReviewMapper;

import java.util.ArrayList;
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

    public List<ReviewDto> findAllReviewsByBookingId(Long bookingId){
        List<ReviewDto> reviewDtos = new ArrayList<>();
        for (Review review :reviewRepository.findByBookingId(bookingId)) {
            ReviewDto reviewDto = ReviewMapper.instance.toDto(review);
            reviewDtos.add(reviewDto);
        }
        return reviewDtos;
    }

    public List<ReviewDto> findReviewsByProperty(Long propertyId){
        List<ReviewDto> reviewDtos = new ArrayList<>();
        for (Review review : reviewRepository.findAllReviewsByIdProperty(propertyId)) {
            ReviewDto reviewDto = ReviewMapper.instance.toDto(review);
            reviewDtos.add(reviewDto);
        }
        return reviewDtos;
    }

    public boolean addReview(ReviewDto reviewDto, Long bookingId, Long userId){
        Booking booking = bookingRepository.findById(bookingId).get();
        Property property = booking.getApartment().getProperty();
        if (booking.getUser().getId().equals(userId)){
            Review review = new Review();
            review.setMessage(reviewDto.getMessage());
            review.setRating(reviewDto.getRating());
            review.setBooking(booking);
            reviewRepository.save(review);

            booking.setReview(review);
            bookingRepository.save(booking);
            property.setRating((property.getRating() + review.getRating())/2);
            propertyRepository.save(property);
            return true;
        }
        return false;
    }

}
