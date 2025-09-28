package com.hotel.hotel_booking_service.service;

import com.hotel.hotel_booking_service.domain.Hotel;
import com.hotel.hotel_booking_service.dto.requestDto.CreateHotelRequest;
import com.hotel.hotel_booking_service.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService {
    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    /**
     * Adds a new hotel to the database.
     *
     * @param req hotel creation request
     * @return saved hotel
     */
    public Hotel addHotel(CreateHotelRequest req) {
        Hotel hotel = new Hotel(req.getName(), req.getLocation(), req.getRooms());
        return hotelRepository.save(hotel);
    }

    /**
     * Retrieves all hotels from the database.
     *
     * @return list of hotels
     */
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }
}