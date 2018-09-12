package com.softserve.edu.bookinglite.controller;

import java.security.Principal;
import java.util.List;

import com.softserve.edu.bookinglite.exception.*;
import com.softserve.edu.bookinglite.service.ReviewService;
import com.softserve.edu.bookinglite.service.dto.CreateReviewDto;
import com.softserve.edu.bookinglite.service.dto.ReviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/booking/{id}/review")
    public ReviewDto getReviewForBooking(@PathVariable("id") Long bookingId) throws ReviewNotFoundExeption {
        return reviewService.findReviewByBookingId(bookingId);
    }

    @GetMapping("property/{id}/reviews/count")
    public int findCountReviewsByIdProperty(@PathVariable("id") Long propertyId) throws PropertyNotFoundException {
        return reviewService.findCountReviewsByPropertyId(propertyId);
    }

    @GetMapping("/property/{id}/reviews")
    public Page<ReviewDto> findPageAllReviewsByIdProperty(@PathVariable("id") Long propertyId,
                                                          @RequestParam("page") int page,
                                                          @RequestParam("size") int size) throws PropertyNotFoundException {
        return reviewService.findPageAllReviewsByPropertyId(propertyId, page, size);
    }

    @PostMapping("/booking/{id}/review")
    public ResponseEntity<Void> saveReview(@RequestBody CreateReviewDto reviewDto,
                                           @PathVariable("id") Long bookingId,
                                           Principal principal)
            throws BookingNotFoundException, ReviewOwnerException, CantLeaveReviewException, ReviewAlreadyExistExeption {
        Long userId = Long.parseLong(principal.getName());
        if (reviewService.addReview(reviewDto, bookingId, userId)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
