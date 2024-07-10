package com.example.bookingproject.Service;

import com.example.bookingproject.Config.BookingType;
import com.example.bookingproject.Dto.BookingPagination;
import com.example.bookingproject.Model.BookingEntity;

import java.util.List;

public interface BookingEntityService {

    BookingPagination getAllAvailableBooking(int pageNo, int pageSize);

    BookingPagination findBookingsByParameters(BookingType bookingType, Boolean occupied, String country, String city, String address, String query, String sort, int pageNo, int pageSize);

    BookingEntity findById(Long bookingId);
}
