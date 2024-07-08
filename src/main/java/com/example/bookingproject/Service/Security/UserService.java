package com.example.bookingproject.Service.Security;



import com.example.bookingproject.Dto.security.RegistrationDto;

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

}
