package com.hotel.hotel_booking_service.service;

import com.hotel.hotel_booking_service.domain.Booking;
import com.hotel.hotel_booking_service.domain.Hotel;
import com.hotel.hotel_booking_service.exception.ApiException;
import com.hotel.hotel_booking_service.exception.ConflictException;
import com.hotel.hotel_booking_service.repository.BookingRepository;
import com.hotel.hotel_booking_service.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final NotificationService notificationService;

    public BookingService(BookingRepository bookingRepository,
                          HotelRepository hotelRepository,
                          NotificationService notificationService) {
        this.bookingRepository = bookingRepository;
        this.hotelRepository = hotelRepository;
        this.notificationService = notificationService;
    }

    /**
     * Retrieves bookings for a given hotel.
     * Optionally filters by start and end date.
     *
     * @param hotelId hotel identifier
     * @param start   optional start date filter
     * @param end     optional end date filter
     * @return list of bookings
     */
    public List<Booking> listBookings(String hotelId, LocalDate start, LocalDate end) {
        // validate hotel exists
        if (!hotelRepository.existsById(hotelId)) {
            throw new ApiException("Hotel not found", 404);
        }

        if (start != null && end != null) {
            return bookingRepository.findByHotelIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(hotelId, end, start);
        } else {
            return bookingRepository.findByHotelId(hotelId);
        }
    }


    /**
     * Creates a booking for a hotel after validating hotel existence,
     * date constraints, and booking conflicts.
     *
     * @param hotelId       hotel identifier
     * @param customerName  customer name
     * @param customerEmail customer email
     * @param customerPhone customer phone
     * @param startDate     booking start date
     * @param endDate       booking end date
     * @return saved booking
     */
    public Booking createBooking(String hotelId, String customerName, String customerEmail, String customerPhone,
                                 LocalDate startDate, LocalDate endDate) {
        // validate hotel exists
        Optional<Hotel> hotelOpt = hotelRepository.findById(hotelId);
        if (hotelOpt.isEmpty()) {
            throw new ApiException("Hotel not found", 404);
        }

        // basic date sanity
        if (endDate.isBefore(startDate) || endDate.isEqual(startDate)) {
            throw new ApiException("endDate must be after startDate", 400);
        }

        // conflict detection (overlap)
        List<Booking> conflicts = bookingRepository.findByHotelIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                hotelId, endDate, startDate
        );

        if (!conflicts.isEmpty()) {
            throw new ApiException("Booking conflict: overlapping booking exists",400);
        }

        Booking booking = new Booking(hotelId, customerName, customerEmail, customerPhone, startDate, endDate);
        Booking saved = bookingRepository.save(booking);

        // notify support (async)
        notificationService.notifySupportOfBooking(saved);

        return saved;
    }
}