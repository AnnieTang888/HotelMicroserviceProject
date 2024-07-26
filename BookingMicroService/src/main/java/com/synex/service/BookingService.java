package com.synex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.synex.domain.Booking;
import com.synex.domain.Guest;
import com.synex.repository.BookingRepository;
import com.synex.repository.GuestRepository;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private GuestRepository guestRepository;

    @Transactional
    public Booking createBooking(Booking booking) throws IllegalArgumentException {
        if (booking.getGuests() == null || booking.getGuests().isEmpty()) {
            throw new IllegalArgumentException("Booking must have at least one guest");
        }

        // Save each guest associated with the booking
        booking.getGuests().forEach(guest -> {
            if (guest != null) {
                guestRepository.save(guest);
            }
        });

        // Save the booking itself
        return bookingRepository.save(booking);
    }
    
    public List<Booking> findBookingsByStatus(String status) {
        return bookingRepository.findByStatus(status);
    }

    public void updateBookingStatus(int bookingId, String newStatus) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(newStatus);
        bookingRepository.save(booking);
    }

   /* // Fetch bookings by username and status
    public List<Booking> getBookingsForUserByUsername(String username, String status) {
        return bookingRepository.findByUserNameAndStatus(username, status);
    }

    // Fetch bookings by email and status
    public List<Booking> getBookingsForUserByEmail(String email, String status) {
        return bookingRepository.findByUserEmailAndStatus(email, status);
    }

    // Update the status of a specific booking
    @Transactional
    public void updateBookingStatus(Long bookingId, String newStatus) {
        bookingRepository.updateBookingStatus(bookingId, newStatus);
    }*/
      
}
