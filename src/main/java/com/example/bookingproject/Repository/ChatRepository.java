package com.example.bookingproject.Repository;


import com.example.bookingproject.Model.Chat;
import com.example.bookingproject.Model.Security.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findByParticipantsContains(UserEntity user);
}
