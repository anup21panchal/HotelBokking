package com.hotel.hotel_booking_service.repository;

import com.hotel.hotel_booking_service.domain.Hotel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HotelRepository extends MongoRepository<Hotel, String> {
}