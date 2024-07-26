package com.synex.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synex.domain.Review;
import com.synex.service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
     
	@Autowired
	ReviewService reviewService;
	
	@PostMapping("/create")
	public ResponseEntity<?> createReview(@RequestBody Review review){
		Review savedReview = reviewService.saveReview(review);
		return ResponseEntity.ok(savedReview);
	}
	
	@GetMapping("/by-booking/{bookingId}")
	public ResponseEntity<?> getReviewByBookingId(@PathVariable int bookingId){
		Optional<Review> review = reviewService.getReviewByBookingId(bookingId);
		return review.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@GetMapping("/average-rating/{hotelId}")
	public ResponseEntity<Double> getAverageRatingByHotelId(@PathVariable int hotelId){
		Double averageRating = reviewService.getAverageRatingByHotelId(hotelId);
		if(averageRating == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(averageRating);
	}
	
	@GetMapping("/by-hotels/{hotelId}")
	public ResponseEntity<List<Review>> getReviewsByHotelId(@PathVariable int hotelId){
		List<Review> reviews = reviewService.getReviewByHotelId(hotelId);
		return ResponseEntity.ok(reviews);
	}
	
}
