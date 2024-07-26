package com.synex.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.synex.domain.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    //List<Review> findByBookingId(int bookingId);
	List<Review> findByBooking_BookingId(int bookingId);
	
	//List<Review> findByHotelId(int hotelId);
	
	@Query("SELECT r FROM Review r JOIN r.booking b WHERE b.hotelId = :hotelId")
    List<Review> findByHotelId(@Param("hotelId") int hotelId);
	
    //@Query("SELECT AVG(r.overallRating) FROM Review r WHERE r.hotelId = ?1")
	//Double findAverageRatingByHotelId(int hotelId);

	@Query("SELECT AVG(r.overallRating) FROM Review r JOIN r.booking b WHERE b.hotelId = :hotelId")
	Double findAverageRatingByHotelId(int hotelId);
	
	//@Query("SELECT AVG(r.overallRating) FROM Review r JOIN r.booking b WHERE b.hotelId = :hotelId AND b.status = 'COMPLETED'")
	//Double findAverageRatingByHotelId(int hotelId);

	  //@Query("SELECT AVG(r.overallRating) FROM Review r WHERE r.hotelId = :hotelId")
	  //Double findAverageRatingByHotelId(int hotelId);
    
	
	
	    
}
