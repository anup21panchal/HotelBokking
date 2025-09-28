package com.hotel.hotel_booking_service.exception;

import com.hotel.hotel_booking_service.dto.responseDto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation errors from @Valid annotated requests.
     *
     * @param ex MethodArgumentNotValidException
     * @return ApiResponse containing validation error messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();

        return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Validation failed", 400, errors));
    }

    /**
     * Handles custom ApiException including ConflictException.
     *
     * @param ex ApiException
     * @return ApiResponse with error message and status code
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Object>> handleApiException(ApiException ex) {
        return ResponseEntity.status(ex.getCode())
                .body(new ApiResponse<>(ex.getMessage(), ex.getCode(), null));
    }

    /**
     * Handles all unexpected exceptions.
     *
     * @param ex generic exception
     * @return ApiResponse with generic error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneral(Exception ex) {
        ex.printStackTrace(); // log stack trace for debugging
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>("Internal server error", 500, null));
    }
}