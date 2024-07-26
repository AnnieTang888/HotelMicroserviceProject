package com.synex.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synex.domain.Hotel;
import com.synex.domain.RoomType;
import com.synex.repository.HotelRepository;
import com.synex.repository.HotelRoomRepository;
import com.synex.repository.RoomTypeRepository;

@Service
public class HotelService {
	
	@Autowired
	HotelRepository hotelRepository;
	
	@Autowired
	HotelRoomRepository hotelRoomRepository;
	
	@Autowired
	RoomTypeRepository roomTypeRepository;
      
	public List<Hotel> searchHotel(String searchString){
		 return hotelRepository.findByHotelNameLikeOrAddressLikeOrCityLike("%"+searchString+"%", "%"+searchString+"%", "%"+searchString+"%");
	}
	
	public List<Hotel> filterHotel(String starRatingFilter, String averagePriceFilter){
		return hotelRepository.findByStarRatingAndAveragePrice(starRatingFilter, averagePriceFilter);
	}
	
	public Optional<Hotel> hotelDetails(int hotelId) {
		return hotelRepository.findById(hotelId);
	}
	
	/*public List<RoomType> getRoomTypesByHotelId(int hotelId){
		return hotelRoomRepository.findDistinctRoomTypesByHotelId(hotelId);
	}*/
	
	/*public List<RoomType> getRoomTypesByHotelId(int hotelId){
		List<Integer> typeIds=hotelRoomRepository.findDistinctRoomTypeIdsByHotelId(hotelId);
		return roomTypeRepository.findAllById(typeIds);
	}*/
	
}
