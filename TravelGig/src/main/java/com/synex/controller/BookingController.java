package com.synex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.synex.client.BookingClient;


@RestController
public class BookingController {
    
    @Autowired
    BookingClient bookingClient;
    
    @PostMapping("/book")
    public ResponseEntity<?> book(@RequestBody JsonNode bookingNode) {
        try {
            JsonNode response = bookingClient.sendBooking(bookingNode);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            		.body("Error processing bookingClient: " + e.getMessage());
        }
    }
    
    @GetMapping("/bookings/status/{status}")
    public ResponseEntity<?> getBookingsByStatus(@PathVariable String status) {
        try {
            JsonNode bookings = bookingClient.getBookingsByStatus(status);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving bookings by status: " + e.getMessage());
        }
    }

    @PostMapping("/bookings/{bookingId}/status")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Long bookingId, @RequestParam String status) {
        try {
            JsonNode response = bookingClient.updateBookingStatus(bookingId, status);
            return ResponseEntity.ok("Booking status updated successfully: " + response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating booking status: " + e.getMessage());
        }
    }
    
    @PostMapping("/cancel/{bookingId}")
    public ResponseEntity<?> cancelBooking(@PathVariable int bookingId) {
        try {
            ResponseEntity<String> response = bookingClient.cancelBooking(bookingId);
            return ResponseEntity.ok("Booking cancelled successfully: " + response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error cancelling booking: " + e.getMessage());
        }
    }
    
    @PostMapping("/review")
    public ResponseEntity<?> submitReview(@RequestBody JsonNode reviewData){
    	try {
    		JsonNode response = bookingClient.submitReview(reviewData);
    		return ResponseEntity.ok(response);  		
    	}catch(Exception e) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    				.body("Error submitting review:" + e.getMessage());
    	}
    }
    
    @GetMapping("/api/hotels/{hotelId}/average-rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable int hotelId){
    	Double averageRating = bookingClient.getAverageRatingForHotel(hotelId);
    	return ResponseEntity.ok(averageRating);
    }
    
    @GetMapping("/api/hotels/{hotelId}/reviews")
    public ResponseEntity<JsonNode> getReviewsByHotelId(@PathVariable int hotelId){
    	JsonNode reviews = bookingClient.getReviewsByHotelId(hotelId);
    	return ResponseEntity.ok(reviews);
    }
}


