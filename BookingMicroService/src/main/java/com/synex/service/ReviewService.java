package com.synex.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synex.domain.Review;
import com.synex.repository.ReviewRepository;

import jakarta.transaction.Transactional;

@Service
public class ReviewService {
	
	@Autowired
	ReviewRepository reviewRepository;
	
	@Transactional
	public Review saveReview(Review review) {
		return reviewRepository.save(review);
	}
	
	public Optional<Review> getReviewByBookingId(int bookingId){
		return reviewRepository.findByBooking_BookingId(bookingId).stream().findFirst();
	}
	
	public Double getAverageRatingByHotelId(int hotelId) {
		Double averageRating = reviewRepository.findAverageRatingByHotelId(hotelId);
		if(averageRating == null) {
			return 0.0;
		}
		return averageRating;
	}
	
	public List<Review> getReviewByHotelId(int hotelId){
	   return reviewRepository.findByHotelId(hotelId);
    }
}
