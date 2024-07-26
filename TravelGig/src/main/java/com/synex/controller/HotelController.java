package com.synex.controller;

import java.security.Principal;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.synex.client.HotelClient;


@RestController
public class HotelController {
	
	@Autowired
	HotelClient hotelClient;


    @GetMapping("/findHotel/{searchString}")
    public JsonNode searchHotel(@PathVariable String searchString) {
        return hotelClient.searchHotel(searchString);
    }

    @GetMapping("/searchHotelDetails/{hotelId}")
    public JsonNode searchHotelDetails(@PathVariable int hotelId) {
        return hotelClient.searchHotelDetails(hotelId);
    }

    @PostMapping("/filterHotel")
    public JsonNode filterHotel(@RequestBody JsonNode json) {
        return hotelClient.searchFilterHotel(json);
    }

    @GetMapping("/fetch-booking-details")
    public ResponseEntity<Map<String, Object>> fetchBookingDetails(
            @RequestParam("hotelId") int hotelId,
            @RequestParam("noGuests") int noGuests,
            @RequestParam("checkInDate") String checkInDate,
            @RequestParam("checkOutDate") String checkOutDate,
            @RequestParam("roomType") String roomType,
            @RequestParam("noRooms") int noRooms) {
        
        Map<String, Object> bookingDetails = hotelClient.getBookingDetails(hotelId, noGuests, checkInDate, checkOutDate, roomType, noRooms);
        return ResponseEntity.ok(bookingDetails);
     }
  
    
    /*@PostMapping("/submit-question")
    public ResponseEntity<String> submitQuestion(@RequestBody Map<String, String> questionDetails, Principal principal) {
        // Include user information in the question details
        questionDetails.put("userId", principal.getName());  

        String response = hotelClient.submitQuestion(questionDetails);
        return ResponseEntity.ok(response);
    }*/
    
    @PostMapping("/submit-question")
    public ResponseEntity<String> submitQuestion(@RequestBody Map<String, String> questionDetails, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User must be authenticated to submit a question.");
        }
        questionDetails.put("userId", principal.getName());
        String response = hotelClient.submitQuestion(questionDetails);
        return ResponseEntity.ok(response);
    }

    
    @PostMapping("/answer-question/{questionId}")
    public ResponseEntity<String> answerQuestion(@PathVariable long questionId, @RequestBody Map<String, String> answerDetails, Principal principal) {
        if (principal != null && principal.getName() != null) {
            //only authenticated admins can answer questions
            answerDetails.put("adminId", principal.getName());  // Use the admin's username or ID as the responder
            String response = hotelClient.answerQuestion(questionId, answerDetails);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
