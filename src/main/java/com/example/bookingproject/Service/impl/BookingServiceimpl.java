package com.example.bookingproject.Service.impl;

import com.example.bookingproject.Config.BookingType;
import com.example.bookingproject.Dto.BookingDto;
import com.example.bookingproject.Dto.BookingPagination;
import com.example.bookingproject.Mapper.BookingMapper;
import com.example.bookingproject.Model.Attachment;
import com.example.bookingproject.Model.BookingEntity;
import com.example.bookingproject.Model.Comment;
import com.example.bookingproject.Model.Security.UserEntity;
import com.example.bookingproject.Repository.BookingEntityRepository;
import com.example.bookingproject.Service.AttachmentService;
import com.example.bookingproject.Service.BookingService;
import com.example.bookingproject.Service.CommentService;
import com.example.bookingproject.Service.Security.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.util.List;

@Service
public class BookingServiceimpl implements BookingService {
    @Autowired
    private BookingEntityRepository bookingRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private AttachmentService attachmentService;
    Page<BookingEntity> bookingPage =null;
    @Override
    public BookingPagination getAllAvailableBooking(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo,pageSize);
        bookingPage = bookingRepository.findAll(pageable);
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
    @Transactional
    @Override
    public BookingPagination findBookingsByParameters(BookingType bookingType, Boolean occupied, String country, String city, String address, String query, String sort, String companyName, int pageNo, int pageSize) {
        Sort sort_object = Sort.unsorted(); // by rating , by bookingsize
        if(sort!=null && !sort.isEmpty())
        {
            if(sort.equals("capacity"))
                sort_object=Sort.by(Sort.Direction.DESC,"capacity");
            else if(sort.equals("price"))
                sort_object=Sort.by(Sort.Direction.DESC,"price");
            else if(sort.equals("rating"))
                sort_object=Sort.by(Sort.Direction.DESC,"rating");

        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort_object);
        if (query != null && query.length() > 0) {
            bookingPage= bookingRepository.getBookingEntitiesByQuery(query, pageable);
        }

        if (query == null) {
            bookingPage= bookingRepository.findByParameters(bookingType, occupied, country, city, address,companyName, pageable);
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

    @Override
    public BookingEntity findById(Long bookingId) {
        return bookingRepository.findById(bookingId).orElse(null);
    }

    @Override
    public BookingEntity saveBooking(BookingDto bookingDto) {
        BookingEntity bookingEntity = BookingMapper.getBookingEntityFromBookingDto(bookingDto);
        return bookingRepository.save(bookingEntity);
    }

    @Override
    public BookingEntity updateBookings(BookingDto bookingDto) {
        BookingEntity bookingEntity = findById(bookingDto.getId());
        return  bookingRepository.save(bookingEntity);
    }

    @Override
    public void deleteBooking(BookingEntity bookingEntity) {
        List<UserEntity> requestingUsers =  bookingEntity.getRequestingUsers();
        if(requestingUsers.size() > 0) {
            for (UserEntity user : requestingUsers) {
                user.getBookRequest().remove(bookingEntity);
                userService.save(user);
            }
        }
        List<UserEntity> confirmedUsers = bookingEntity.getConfirmedUsers();
        if(confirmedUsers.size() > 0) {
            for (UserEntity user : confirmedUsers) {
                user.getUserBooks().remove(bookingEntity);
                userService.save(user);
            }
        }
        List<Comment> comments = bookingEntity.getComments();
        if(comments.size() > 0)
        {
            for(Comment comment : comments)
            {
                commentService.delete(comment);
            }
        }
        List<Attachment> attachments = bookingEntity.getAttachments();
        if(attachments.size()>0)
        {
            for(Attachment attachment: attachments)
            {
                attachmentService.delete(attachment);
            }
        }
        bookingRepository.delete(bookingEntity);
    }

    @Override
    public List<BookingEntity> findBookingsByCompanyName(String companyName) {
        return bookingRepository.findBookingEntitiesByCompanyName(companyName);
    }


}
