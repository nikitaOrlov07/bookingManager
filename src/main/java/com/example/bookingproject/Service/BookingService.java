package com.example.bookingproject.Service;

import com.example.bookingproject.Config.BookingType;
import com.example.bookingproject.Dto.BookingDto;
import com.example.bookingproject.Dto.BookingPagination;
import com.example.bookingproject.Model.BookingEntity;
import com.example.bookingproject.Model.Security.UserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookingService {

    BookingPagination getAllAvailableBooking(int pageNo, int pageSize);

    BookingPagination findBookingsByParameters(BookingType bookingType, Boolean occupied, String country, String city, String address, String query, String sort, String companyName, int pageNo, int pageSize);

    BookingEntity findById(Long bookingId);

    BookingEntity saveBooking(BookingDto bookingDto);

    BookingEntity updateBookings(BookingDto bookingDto);

    void deleteBooking(BookingEntity bookingEntity) throws Exception;

    List<BookingEntity> findBookingsByCompanyName(String companyName);

    void uploadFile(MultipartFile file, Long id) throws Exception;
}
