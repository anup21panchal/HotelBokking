package com.hotel.hotel_booking_service.repository;

import com.hotel.hotel_booking_service.domain.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {
    // find bookings for hotel that overlap with given dates
    List<Booking> findByHotelIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            String hotelId, LocalDate endDate, LocalDate startDate);

    // find all bookings for a hotel
    List<Booking> findByHotelId(String hotelId);
}