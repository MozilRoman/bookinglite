package com.softserve.edu.bookinglite.controller;

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
    public List<ReviewDto> getAllReviewsForBooking(@PathVariable ("id") Long bookingId){
        return reviewService.findAllReviewsByBookingId(bookingId);
    }

//    @GetMapping("/booking/{id}/review")
//    public ReviewDto getReviewByUser(@PathVariable ("id") Long bookingId, Principal principal){
//        Long userId = Long.parseLong(principal.getName());
//        return reviewService.findReviewByUserId(bookingId, userId);
//    }

    @PostMapping("/booking/{id}/review")
    public ResponseEntity<Void> saveReview(@RequestBody ReviewDto reviewDto,
                                           @PathVariable ("id") Long bookingId,
                                           Principal principal){
        Long userId = Long.parseLong(principal.getName());
        if (reviewService.addReview(reviewDto, bookingId, userId)){
            return new ResponseEntity<Void>(HttpStatus.CREATED);
        }
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }

}
