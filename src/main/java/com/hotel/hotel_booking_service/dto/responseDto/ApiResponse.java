package com.hotel.hotel_booking_service.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiResponse<T> {
    private String message;
    private int code;
    private T data;

    public static <T> ApiResponse<T> success(String msg, T data) {
        return new ApiResponse<>(msg, 200, data);
    }
    public static <T> ApiResponse<T> created(String msg, T data) {
        return new ApiResponse<>(msg, 201, data);
    }
    public static <T> ApiResponse<T> error(String msg, int code) {
        return new ApiResponse<>(msg, code, null);
    }
}