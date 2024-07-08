package com.example.bookingproject.Service.impl;


import com.example.bookingproject.Model.Chat;
import com.example.bookingproject.Model.Message;
import com.example.bookingproject.Model.Security.UserEntity;
import com.example.bookingproject.Repository.MessageRepository;
import com.example.bookingproject.Service.ChatService;
import com.example.bookingproject.Service.MessageService;
import com.example.bookingproject.Service.Security.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceimpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ChatService chatService;
    @Override
    public List<Message> findAllChatMessage(Long chaId) {
        return messageRepository.findAllByChatId(chaId);
    }
    @Override
    public Message saveMessage(Message message, Long chatId, UserEntity user) {

        Chat chat = chatService.findById(chatId).get();

        message.setChat(chat);

        message.setAuthor(user.getUsername());
        chatService.updateChat(chat);
        return  messageRepository.save(message);
    }
    @Override
    public Optional<Message> findById(Long message) {
        return messageRepository.findById(message);
    }
    @Transactional
    @Override
    public void deleteMessage(Message message, UserEntity user, Chat chat) {

    }
    
}
