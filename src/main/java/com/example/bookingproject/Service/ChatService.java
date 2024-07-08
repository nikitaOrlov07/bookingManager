package com.example.bookingproject.Service;

import com.example.bookingproject.Model.Chat;
import com.example.bookingproject.Model.Security.UserEntity;

import java.util.List;
import java.util.Optional;

public interface ChatService {
    Optional<Chat> findById(Long chatId);

    void updateChat(Chat chat);

    Chat findOrCreateChat(UserEntity currentUser, UserEntity secondUser);

    void save(Chat chat);

    void delete(Chat chat);

    List<Chat> findAllByParticipants(UserEntity deletedUser);

    void clearMessages(Chat chat);
}
