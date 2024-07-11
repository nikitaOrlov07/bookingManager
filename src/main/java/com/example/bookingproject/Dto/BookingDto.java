package com.example.bookingproject.Dto;

import com.example.bookingproject.Config.BookingTime;
import com.example.bookingproject.Config.BookingType;
import com.example.bookingproject.Config.Currency;
import com.example.bookingproject.Model.Attachment;
import com.example.bookingproject.Model.Security.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public class BookingDto {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotEmpty(message = "You must provide title")
        private String title;

        @NotNull(message = "You must provide type of booking")
        private BookingType type;

        private String description;
        private String amenities;

        @NotNull(message = "You must provide price")
        @DecimalMin(value = "0.0", inclusive = false, message = "The value must be greater than 0")
        private Double price;

        @NotNull(message = "You must provide currency")
        private Currency currency;

        @NotNull(message= "You must provide time")
        private BookingTime bookingTime;
        // location
        @NotEmpty(message = "You must provide country")
        private String country;

        @NotEmpty(message = "You must provide city")
        private String city;

        @NotEmpty(message = "You must provide address")
        private String address;

        private String companyName;
        private int capacity;
        private int numberOfRooms;

        private List<Attachment> attachments; // for photos

        private UserEntity author;
    }
