package com.hotel.hotel_booking_service.dto.requestDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CreateHotelRequest {
    @NotBlank(message = "Hotel name is required")
    private String name;

    @NotBlank(message = "Hotel location is required")
    private String location;

    @Min(value = 1, message = "Rooms must be at least 1")
    private int rooms;

    // Getters & Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getRooms() { return rooms; }
    public void setRooms(int rooms) { this.rooms = rooms; }
}