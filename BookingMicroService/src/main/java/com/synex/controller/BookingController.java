package com.synex.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.synex.domain.Booking;
import com.synex.service.BookingService;

@RestController
public class BookingController {
	
	@Autowired
	BookingService bookingService;
	
	@RequestMapping(value = "/booking", method = RequestMethod.POST)
	public ResponseEntity<?> creatBooking(@RequestBody Booking booking){
		try {
			Booking savedBooking = bookingService.createBooking(booking);
			return ResponseEntity.ok(Map.of("message","Booking successful!","bookingId"
					,savedBooking.getBookingId()));			
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to create booking:"+e.getMessage());
		}
	}
	
	@GetMapping("/bookings/status/{status}")
    public ResponseEntity<List<Booking>> getBookingsByStatus(@PathVariable String status) {
        try {
            List<Booking> bookings = bookingService.findBookingsByStatus(status);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
        	System.out.println("Failed to retrieve bookings: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/cancel/{bookingId}")
    public ResponseEntity<String> cancelBooking(@PathVariable int bookingId) {
        try {
            bookingService.updateBookingStatus(bookingId, "CANCELLED");
            return ResponseEntity.ok("Booking cancelled successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error cancelling booking");
        }
    }
	
	/*// Get bookings by username and status
    @GetMapping("/by-username")
    public ResponseEntity<?> getBookingsByUsername(@RequestParam String username, @RequestParam String status) {
        try {
            List<Booking> bookings = bookingService.getBookingsForUserByUsername(username, status);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch bookings: " + e.getMessage());
        }
    }

    // Update the status of a specific booking
    @PostMapping("/{bookingId}/status")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Long bookingId, @RequestParam String status) {
        try {
            bookingService.updateBookingStatus(bookingId, status);
            return ResponseEntity.ok("Status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating booking status: " + e.getMessage());
        }
    }*/
	
	
}
