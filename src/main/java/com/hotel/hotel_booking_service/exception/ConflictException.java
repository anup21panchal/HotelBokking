package com.hotel.hotel_booking_service.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) { super(message); }
}