package com.synex.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synex.domain.Hotel;
import com.synex.repository.HotelRepository;

@Service
public class BookingService {
      
	@Autowired 
	HotelRepository hotelRepository;
	
	public Map<String, Object> fetchBookDetails(int hotelId, int noGuests, String checkInDate, 
	     String checkOutDate, String roomType, int noRooms){
		 Map<String, Object> bookingDetails = new HashMap<>();
		 Optional<Hotel> hotelOpt = hotelRepository.findById(hotelId);
		 if(hotelOpt.isPresent()) {
			Hotel hotel = hotelOpt.get();
			double discount = hotel.getDiscount();
			double pricePerRoom = 100;
			double totalPrice = (pricePerRoom * noRooms) - discount;
			
			bookingDetails.put("hotelId", hotelId);
			bookingDetails.put("hotelName", hotel.getHotelName()); // Hotel Name
	        bookingDetails.put("customerMobile", "650-5579832"); 
			bookingDetails.put("noGuests", noGuests);
			bookingDetails.put("checkInDate", checkInDate);
			bookingDetails.put("checkOutDate", checkOutDate);
			bookingDetails.put("roomType", roomType);
			bookingDetails.put("noRooms", noRooms);
			bookingDetails.put("discount", discount);
			bookingDetails.put("price", totalPrice);
			
		}
		return bookingDetails;
	}
}
