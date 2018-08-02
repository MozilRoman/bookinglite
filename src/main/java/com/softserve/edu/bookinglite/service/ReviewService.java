package com.softserve.edu.bookinglite.service;

import com.softserve.edu.bookinglite.entity.Booking;
import com.softserve.edu.bookinglite.entity.Property;
import com.softserve.edu.bookinglite.entity.Review;
import com.softserve.edu.bookinglite.repository.BookingRepository;
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

    @Autowired
    public ReviewService(BookingRepository bookingRepository, ReviewRepository reviewRepository) {
        this.bookingRepository = bookingRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<ReviewDto> findAllReviewsByBookingId(Long bookingId){
        List<Review> reviews = reviewRepository.findByBookingId(bookingId);
        List<ReviewDto> reviewDtos = new ArrayList<>();
        for (Review review :reviewRepository.findByBookingId(bookingId)) {
            ReviewDto reviewDto = ReviewMapper.instance.toDto(review);
            reviewDtos.add(reviewDto);
        }
        return reviewDtos;
    }

    public ReviewDto findReviewByUserId(Long bookingId, Long userId){
        Booking booking = bookingRepository.findById(bookingId).get();
        if (booking.getUser().getId().equals(userId)){
            return ReviewMapper.instance.toDto(booking.getReview());
        }
        return null;
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
            property.setRating((property.getRating() + review.getRating())/2);
            return true;
        }
        return false;
    }

}
