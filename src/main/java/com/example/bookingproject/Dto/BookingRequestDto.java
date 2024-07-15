package com.example.bookingproject.Dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class BookingRequestDto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String bookingTitle;
    private String username;

    public BookingRequestDto(String bookingTitle, String username) {
        this.bookingTitle = bookingTitle;
        this.username = username;
    }

}
