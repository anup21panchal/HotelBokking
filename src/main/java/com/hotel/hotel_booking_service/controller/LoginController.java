package com.hotel.hotel_booking_service.controller;

import com.hotel.hotel_booking_service.config.JwtUtil;
import com.hotel.hotel_booking_service.domain.User;
import com.hotel.hotel_booking_service.dto.responseDto.ApiResponse;
import com.hotel.hotel_booking_service.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static record LoginRequest(@Email String email, @NotBlank String password) {}

    /**
     * Authenticates a user with email and password.
     * Returns a JWT token if login is successful.
     *
     * @param req login request containing email and password
     * @return ApiResponse containing a JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(@RequestBody LoginRequest req) {
        User user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }

        if (!"ACTIVE".equals(user.getStatus())) {
            throw new RuntimeException("User account is not active");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles());

        String token = JwtUtil.generateToken(user.getEmail(), claims, 86400000); // 1 day validity

        Map<String, String> data = new HashMap<>();
        data.put("token", token);

        return ResponseEntity.ok(new ApiResponse<>("Login successful", 200, data));
    }
}