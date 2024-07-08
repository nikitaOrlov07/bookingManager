package com.example.bookingproject.Service.impl;

import com.example.bookingproject.Model.BookingEntity;
import com.example.bookingproject.Repository.BookingEntityRepository;
import com.example.bookingproject.Service.BookingEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingEntityServiceimpl implements BookingEntityService{
    @Autowired
    private BookingEntityRepository bookingEntityRepository;

    @Override
    public List<BookingEntity> getAllAvailableBooking() {
        return bookingEntityRepository.getBookEntitiesByOccupiedFalse();
    }
}
