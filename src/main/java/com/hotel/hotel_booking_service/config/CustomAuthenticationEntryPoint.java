package com.hotel.hotel_booking_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.hotel_booking_service.dto.responseDto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        ApiResponse<String> apiResponse = new ApiResponse<>(
                "Invalid or expired token", 401, null
        );

        response.getWriter().write(mapper.writeValueAsString(apiResponse));
    }
}