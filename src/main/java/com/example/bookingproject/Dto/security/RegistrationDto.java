package com.example.bookingproject.Dto.security;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RegistrationDto {
    private Long id;
    @NotEmpty(message ="Must be not empty")
    private String username;
    @NotEmpty(message ="Must be not empty")
    private String email;
    @NotEmpty(message ="Must be not empty")
    private String password;
    private String town;
    private Long phoneNumber;
}
