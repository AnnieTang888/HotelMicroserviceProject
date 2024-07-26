package com.synex.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.synex.domain.HotelRoom;
import com.synex.domain.RoomType;

@Repository
public interface HotelRoomRepository extends JpaRepository<HotelRoom, Integer> {
    
	/*@Query("SELECT DISTINCT hr.type FROM HotelRoom hr WHERE hr.hotel.hotelId = :hotelId")
    List<RoomType> findDistinctRoomTypesByHotelId(@Param("hotelId") int hotelId);*/
	
	
	/*@Query(value = "SELECT DISTINCT type_id FROM hotel_rooms WHERE hotel_id = :hotelId", nativeQuery = true)
	List<Integer> findDistinctRoomTypeIdsByHotelId(@Param("hotelId") int hotelId);*/

}

