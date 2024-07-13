package com.example.bookingproject.Model;

import com.example.bookingproject.Model.Security.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private BookingEntity booking;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private int value;

    public Rating(BookingEntity booking, UserEntity user, int value) {
        this.booking = booking;
        this.user = user;
        this.value = value;
    }



}