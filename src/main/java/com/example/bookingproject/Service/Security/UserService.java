package com.example.bookingproject.Service.Security;



import com.example.bookingproject.Dto.BookingRequestDto;
import com.example.bookingproject.Dto.security.RegistrationDto;

import com.example.bookingproject.Model.Comment;
import com.example.bookingproject.Model.Security.UserEntity;

import java.util.List;

public interface UserService {
    void saveUser(RegistrationDto registrationDto);

    UserEntity findByEmail(String email);

    UserEntity findByUsername(String username);

    List<UserEntity> findAllUsers();

    UserEntity findById(Long userId);

    List<UserEntity> search(String query, String type);

    void save(UserEntity currentUser);

    List<UserEntity> findAllByLikedComments(Comment comment);

    List<UserEntity> findAllByDislikedComments(Comment comment);

    void actionComment(String dislike, Comment comment);
    List<BookingRequestDto> findUserBookingsWithRequests(UserEntity user);

    List<BookingRequestDto> findUserConfirmsBookings(UserEntity user);

    List<UserEntity> findAllBookingsCreators();


}
