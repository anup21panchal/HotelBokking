package com.hotel.hotel_booking_service.controller;

import com.hotel.hotel_booking_service.domain.Hotel;
import com.hotel.hotel_booking_service.dto.requestDto.CreateHotelRequest;
import com.hotel.hotel_booking_service.dto.responseDto.ApiResponse;
import com.hotel.hotel_booking_service.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {
    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    /**
     * Creates a new hotel record.
     *
     * @param req hotel creation request
     * @return ApiResponse containing the created hotel
     */
    @PostMapping("/addHotel")
    public ResponseEntity<ApiResponse<Hotel>> createHotel(@Valid @RequestBody CreateHotelRequest req) {
        Hotel saved = hotelService.addHotel(req);
        return ResponseEntity.status(201).body(new ApiResponse<>("Hotel created", 201, saved));
    }

    /**
     * Retrieves all hotels.
     *
     * @return ApiResponse containing the list of hotels
     */
    @GetMapping("/getAllHotels")
    public ResponseEntity<ApiResponse<List<Hotel>>> getHotels() {
        List<Hotel> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(new ApiResponse<>("OK", 200, hotels));
    }
}