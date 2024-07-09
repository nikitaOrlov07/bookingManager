package com.example.bookingproject.Service.impl;

import com.example.bookingproject.Config.BookingType;
import com.example.bookingproject.Dto.BookingPagination;
import com.example.bookingproject.Model.BookingEntity;
import com.example.bookingproject.Repository.BookingEntityRepository;
import com.example.bookingproject.Service.BookingEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.util.List;

@Service
public class BookingEntityServiceimpl implements BookingEntityService{
    @Autowired
    private BookingEntityRepository bookingRepository;
    Page<BookingEntity> bookingPage =null;
    @Override
    public BookingPagination getAllAvailableBooking() {

        List<BookingEntity> bookingList = bookingPage.getContent();
        BookingPagination bookingPagination = BookingPagination.builder()
                .data(bookingList)
                .pageNo(bookingPage.getNumber())
                .pageSize(bookingPage.getSize())
                .totalElements(bookingPage.getTotalElements())
                .totalPages(bookingPage.getTotalPages())
                .last(bookingPage.isLast())
                .build();
        return bookingPagination;
    }

    @Override
    public BookingPagination findBookingsByParameters(BookingType bookingType, boolean occupied, String country, String city, String address, String query, String sort, int pageNo, int pageSize) {
        Sort sort_object = Sort.unsorted(); // by rating , by bookingsize
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort_object);


        if (query != null && query.length() > 0) {
            bookingPage= bookingRepository.getBookingEntitiesByQuery(query, pageable);
        }

        if (query == null) {
            bookingPage= bookingRepository.findByParameters(bookingType, occupied, country, city, address, pageable);
        }

        List<BookingEntity> bookingList = bookingPage.getContent();
        BookingPagination bookingPagination = BookingPagination.builder()
                .data(bookingList)
                .pageNo(bookingPage.getNumber())
                .pageSize(bookingPage.getSize())
                .totalElements(bookingPage.getTotalElements())
                .totalPages(bookingPage.getTotalPages())
                .last(bookingPage.isLast())
                .build();
        return bookingPagination;

    }


}
