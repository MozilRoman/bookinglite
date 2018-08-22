package com.softserve.edu.bookinglite.controller;

import com.softserve.edu.bookinglite.exception.BookingNotFoundException;
import com.softserve.edu.bookinglite.exception.CantLeaveReviewException;
import com.softserve.edu.bookinglite.exception.PropertyNotFoundException;
import com.softserve.edu.bookinglite.exception.ReviewOwnerException;
import com.softserve.edu.bookinglite.service.ReviewService;
import com.softserve.edu.bookinglite.service.dto.ReviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/booking/{id}/review")
    public ReviewDto getReviewForBooking(@PathVariable ("id") Long bookingId){
        return reviewService.findReviewByBookingId(bookingId);
    }

    @GetMapping("/property/{id}/reviews")
    public List<ReviewDto> findAllReviewsByIdProperty(@PathVariable ("id") Long propertyId)
            throws PropertyNotFoundException {
        return reviewService.findAllReviewsByPropertyId(propertyId);
    }

    @PostMapping("/booking/{id}/review")
    public ResponseEntity<Void> saveReview(@RequestBody ReviewDto reviewDto,
                                           @PathVariable ("id") Long bookingId,
                                           Principal principal)
            throws BookingNotFoundException, ReviewOwnerException, CantLeaveReviewException {
        Long userId = Long.parseLong(principal.getName());
        if (reviewService.addReview(reviewDto, bookingId, userId)){
            return new ResponseEntity<Void>(HttpStatus.CREATED);
        }
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }

}
