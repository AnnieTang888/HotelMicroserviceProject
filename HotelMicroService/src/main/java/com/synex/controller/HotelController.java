package com.synex.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.synex.domain.Hotel;
import com.synex.domain.RoomType;
import com.synex.services.BookingService;
import com.synex.services.HotelService;

@RestController
public class HotelController {
    
	@Autowired
	HotelService hotelService;
	
	@Autowired
	BookingService bookingService;
	
	@RequestMapping(value="/searchHotel/{searchString}", method = RequestMethod.GET)
	public List<Hotel> searchHotel(@PathVariable String searchString){
		 return hotelService.searchHotel(searchString);
	}
	
	@RequestMapping(value="/filterHotel", method = RequestMethod.POST)
	public List<Hotel> filterHotel(@RequestBody JsonNode json){
		 List<Hotel> filteredList = new ArrayList<>();
		 String searchString = json.get("searchString").asText();
		 String rating = json.get("ratings").asText();
		 int amount = json.get("amount").asInt();
		 List<Hotel> listHotel = hotelService.searchHotel(searchString);
		 Iterator<Hotel> itr = listHotel.iterator();
		 String[] ratingArray = rating.split(",");
		 while(itr.hasNext()) {
			 Hotel hotel = itr.next();
			 if(hotel.getAveragePrice()<=amount) {
				 for(String rat : ratingArray) {
					 int ratingInt = Integer.parseInt(rat);
					 if(ratingInt == hotel.getStarRating()) {
						 filteredList.add(hotel);
					 }
				 }
			 }
		 }		 
		 return filteredList;	
    }
	
	@RequestMapping(value="/searchHotelDetails/{hotelId}", method = RequestMethod.GET)
	public Optional<Hotel> searchHotelDetails(@PathVariable int hotelId){
		 return hotelService.hotelDetails(hotelId);
	}
	
	@RequestMapping(value = "/booking-details", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getBookingDetails(
	    @RequestParam("hotelId") int hotelId,
	    @RequestParam("noGuests") int noGuests,
	    @RequestParam("checkInDate") String checkInDate,
	    @RequestParam("checkOutDate") String checkOutDate,
	    @RequestParam("roomType") String roomType,
	    @RequestParam("noRooms") int noRooms) {

	    Map<String, Object> bookingDetails = bookingService.fetchBookDetails(hotelId, noGuests, checkInDate, checkOutDate, roomType, noRooms);	    
	    return ResponseEntity.ok(bookingDetails);
	}

	
	
	
/*
	@RequestMapping(value="/fetchHotelRoomTypes/{hotelId}", method = RequestMethod.GET)
	public List<RoomType> fetchHotelRoomTypes(@PathVariable int hotelId){
		return hotelService.getRoomTypesByHotelId(hotelId);
	}*/
	
}
