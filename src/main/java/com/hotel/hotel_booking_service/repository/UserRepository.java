package com.hotel.hotel_booking_service.repository;

import com.hotel.hotel_booking_service.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByConfirmationToken(String token);
}
