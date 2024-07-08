package com.example.bookingproject.Service.Security;

import com.example.bookingproject.Dto.security.RegistrationDto;
import com.example.bookingproject.Model.Chat;
import com.example.bookingproject.Model.Message;

import com.example.bookingproject.Model.Security.RoleEntity;
import com.example.bookingproject.Model.Security.UserEntity;
import com.example.bookingproject.Repository.MessageRepository;
import com.example.bookingproject.Repository.Security.RoleRepository;
import com.example.bookingproject.Repository.Security.UserRepository;
import com.example.bookingproject.Service.ChatService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceimpl implements UserService{
    private UserRepository userRepository; private RoleRepository roleRepository;  // implements methods from repositories
    private PasswordEncoder passwordEncoder;  private MessageRepository messageRepository; private ChatService chatService;
    @Autowired
    public UserServiceimpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, MessageRepository messageRepository , ChatService chatService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.messageRepository = messageRepository;
        this.chatService=chatService;
    }

    @Override
    public void saveUser(RegistrationDto registrationDto) {
        UserEntity userEntity = new UserEntity(); // we cant save RegistrationDto to the database because it`s totally different entity
        // create something like mapper
        userEntity.setUsername(registrationDto.getUsername());
        userEntity.setEmail(registrationDto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        userEntity.setTown(registrationDto.getTown());
        userEntity.setPhoneNumber(registrationDto.getPhoneNumber());

        RoleEntity role = roleRepository.findByName("USER");// по факту "USER"  записывается в переменную role (- В этой строке мы ищем объект RoleEntity, представляющий роль "USER" в системе.)
        userEntity.setRoles(Arrays.asList(role));// даем нашему userEntity (юзеру) роль "USER" (мы назначаем найденную роль "USER" пользователю, устанавливая список ролей пользователя в качестве списка,
        //----------------------------------------------------------------
        userRepository.save(userEntity);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    @Transactional
    @Override
    public UserEntity findByUsername(String username) {
       return  userRepository.findByUsername(username);
    }

    @Override
    public UserEntity findById(Long userId) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        return optionalUser.orElse(null);
    }




    @Override
    public List<UserEntity> search(String query, String type) {
       return null;
    }

    @Override
    public void save(UserEntity currentUser) {
        userRepository.save(currentUser);
    }



    @Override
    public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }






}
