package com.hotel.hotel_booking_service.controller;

import com.hotel.hotel_booking_service.domain.Booking;
import com.hotel.hotel_booking_service.dto.requestDto.CreateBookingRequest;
import com.hotel.hotel_booking_service.dto.responseDto.ApiResponse;
import com.hotel.hotel_booking_service.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/hotels/{hotelId}/bookings")
public class BookingController {

    private final BookingService bookingService;
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Retrieves a list of bookings for a given hotel.
     * Optionally filters by date range if startDate and endDate are provided.
     *
     * @param hotelId   the hotel identifier
     * @param startDate optional filter start date
     * @param endDate   optional filter end date
     * @return ApiResponse containing the list of bookings
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Booking>>> listBookings(
            @PathVariable String hotelId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<Booking> result = bookingService.listBookings(hotelId, startDate, endDate);
        return ResponseEntity.ok(new ApiResponse<>("OK", 200, result));
    }

    /**
     * Creates a new booking for the specified hotel.
     * Requires "staff" or "reception" role.
     *
     * @param hotelId the hotel identifier
     * @param req     booking creation request payload
     * @return ApiResponse containing the created booking
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('staff','reception')")
    public ResponseEntity<ApiResponse<Booking>> createBooking(
            @PathVariable String hotelId,
            @Valid @RequestBody CreateBookingRequest req) {

        Booking saved = bookingService.createBooking(hotelId,
                req.getCustomerName(), req.getCustomerEmail(), req.getCustomerPhone(),
                req.getStartDate(), req.getEndDate());

        return ResponseEntity.status(201).body(new ApiResponse<>("Booking created", 201, saved));
    }
}