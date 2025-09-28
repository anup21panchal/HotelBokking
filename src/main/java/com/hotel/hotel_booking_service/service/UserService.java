package com.hotel.hotel_booking_service.service;

import com.hotel.hotel_booking_service.domain.User;
import com.hotel.hotel_booking_service.exception.ApiException;
import com.hotel.hotel_booking_service.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       NotificationService notificationService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user, hashes password, assigns role,
     * generates confirmation token, and sends confirmation email.
     *
     * @param name       user name
     * @param email      user email
     * @param rawPassword raw password
     * @param role       user role (default "guest" if null)
     * @return saved user
     */
    public User registerUser(String name, String email, String rawPassword, String role) {
        Optional<User> existing = userRepository.findByEmail(email);
        if (existing.isPresent()) {
            throw new ApiException("Email already registered", 409);
        }

        String token = UUID.randomUUID().toString();
        String hash = passwordEncoder.encode(rawPassword);

        // Use role passed from request (default to guest if null)
        String[] roles = role != null ? new String[]{role} : new String[]{"guest"};

        User user = new User(name, email, hash, roles, "PENDING", token);
        User saved = userRepository.save(user);

        // Send confirmation email
        notificationService.sendUserConfirmation(saved, token);

        return saved;
    }

    /**
     * Confirms a user's account using the confirmation token.
     *
     * @param token confirmation token
     * @return updated user with ACTIVE status
     */
    public User confirmUser(String token) {
        var opt = userRepository.findByConfirmationToken(token);
        if (opt.isEmpty()) {
            throw new ApiException("Invalid or expired confirmation token", 400);
        }
        User user = opt.get();
        user.setStatus("ACTIVE");
        user.setConfirmationToken(null); // remove token after confirm
        return userRepository.save(user);
    }
}