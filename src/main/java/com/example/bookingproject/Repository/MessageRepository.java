package com.example.bookingproject.Repository;


import com.example.bookingproject.Model.Chat;
import com.example.bookingproject.Model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
List<Message> findAllByChatId(Long chatId);

void deleteAllByChat(Chat chat);
}
