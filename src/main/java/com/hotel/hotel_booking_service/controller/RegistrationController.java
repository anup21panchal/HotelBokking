package com.hotel.hotel_booking_service.controller;

import com.hotel.hotel_booking_service.domain.User;
import com.hotel.hotel_booking_service.dto.requestDto.RegisterRequest;
import com.hotel.hotel_booking_service.dto.responseDto.ApiResponse;
import com.hotel.hotel_booking_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

    private final UserService userService;
    public RegistrationController(UserService userService) { this.userService = userService; }

    /**
     * Registers a new user with provided details.
     * Sends a confirmation email with token.
     *
     * @param req user registration request
     * @return ApiResponse containing the created user
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@Valid @RequestBody RegisterRequest req) {
        User user = userService.registerUser(req.name(), req.email(), req.password(), req.role());
        return ResponseEntity.status(201).body(new ApiResponse<>("User registered. Please check email to confirm.", 201, user));
    }

    /**
     * Confirms user account using the provided confirmation token.
     *
     * @param token confirmation token
     * @return ApiResponse with confirmation result message
     */
    @GetMapping("/confirm")
    public ResponseEntity<ApiResponse<String>> confirm(@RequestParam("token") String token) {
        userService.confirmUser(token);
        return ResponseEntity.ok(new ApiResponse<>("Account confirmed. You can now login.", 200, null));
    }
}