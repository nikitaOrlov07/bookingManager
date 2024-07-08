package com.example.bookingproject.Service;

import com.example.bookingproject.Model.BookingEntity;

import java.util.List;

public interface BookingEntityService {
    List<BookingEntity> getAllAvailableBooking();
}
