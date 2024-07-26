package com.synex.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.synex.domain.Booking;

import jakarta.transaction.Transactional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
	
	List<Booking> findByStatus(String status);
	
	
    /*List<Booking> findByUserNameAndStatus(String userName, String status);
    List<Booking> findByUserEmailAndStatus(String userEmail, String status);

    @Modifying
    @Transactional
    @Query("UPDATE Booking b SET b.status = :status WHERE b.bookingId = :bookingId")
    void updateBookingStatus(@Param("bookingId") Long bookingId, @Param("status") String status);*/
}


