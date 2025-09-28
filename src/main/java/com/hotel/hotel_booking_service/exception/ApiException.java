package com.hotel.hotel_booking_service.exception;

/**
 * Base custom exception for API errors.
 * - Holds an HTTP status code (like 400, 404, 409).
 * - GlobalExceptionHandler will catch this and wrap it in ApiResponse.
 */
public class ApiException extends RuntimeException {
    private final int code;

    public ApiException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}