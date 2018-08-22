package com.softserve.edu.bookinglite.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.edu.bookinglite.exception.BookingNotFoundException;
import com.softserve.edu.bookinglite.exception.CantLeaveReviewException;
import com.softserve.edu.bookinglite.exception.PropertyNotFoundException;
import com.softserve.edu.bookinglite.exception.ReviewOwnerException;
import com.softserve.edu.bookinglite.service.ReviewService;
import com.softserve.edu.bookinglite.service.dto.ReviewDto;

@RestController
@RequestMapping("/api")
public class ReviewController {

	private final ReviewService reviewService;

	@Autowired
	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}

	@GetMapping("/booking/{id}/review")
	public List<ReviewDto> getReviewForBooking(@PathVariable("id") Long bookingId) {
		return reviewService.findReviewByBookingId(bookingId);
	}

	@GetMapping("/property/{id}/reviews")
	public List<ReviewDto> findAllReviewsByIdProperty(@PathVariable("id") Long propertyId)
			throws PropertyNotFoundException {
		return reviewService.findAllReviewsByPropertyId(propertyId);
	}
	
	@GetMapping("property/{id}/reviews/count")
	public int findCountReviewsByIdProperty(@PathVariable("id") Long propertyId) throws PropertyNotFoundException {
		return reviewService.findCountReviewsByPropertyId(propertyId);
	}

	@PostMapping("/booking/{id}/review")
	public ResponseEntity<Void> saveReview(@RequestBody ReviewDto reviewDto, @PathVariable("id") Long bookingId,
			Principal principal) throws BookingNotFoundException, ReviewOwnerException, CantLeaveReviewException {
		Long userId = Long.parseLong(principal.getName());
		if (reviewService.addReview(reviewDto, bookingId, userId)) {
			return new ResponseEntity<Void>(HttpStatus.CREATED);
		}
		return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
	}

	
	
}
