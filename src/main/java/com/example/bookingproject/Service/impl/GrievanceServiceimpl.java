package com.example.bookingproject.Service.impl;

import com.example.bookingproject.Config.GrievanceStatus;
import com.example.bookingproject.Model.Chat;
import com.example.bookingproject.Model.Grievance;
import com.example.bookingproject.Model.Message;
import com.example.bookingproject.Model.Security.UserEntity;
import com.example.bookingproject.Repository.GrievanceRepository;
import com.example.bookingproject.Repository.Security.UserRepository;
import com.example.bookingproject.Service.ChatService;
import com.example.bookingproject.Service.GrievanceService;
import com.example.bookingproject.Service.MessageService;
import com.example.bookingproject.WebSocketConf.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GrievanceServiceimpl implements GrievanceService {
    private GrievanceRepository grievanceRepository;
    private UserRepository userRepository;
    private ChatService chatService;
    private MessageService messageService;

    @Autowired
    public GrievanceServiceimpl(GrievanceRepository grievanceRepository , UserRepository userRepository , ChatService chatService, MessageService messageService) {
        this.grievanceRepository = grievanceRepository;
        this.userRepository = userRepository;
        this.chatService = chatService;
        this.messageService = messageService;
    }

    @Override
    public void createGrievence(UserEntity currentUser, Long reportedUserId, String text) {
        UserEntity reportedUser = userRepository.findById(reportedUserId) .orElseThrow(() -> new RuntimeException("user not found"));
        Grievance grievance = Grievance.builder()
                .text(text)
                .complainant(currentUser)
                .reportedUser(reportedUser)
                .createdAt(LocalDateTime.now())
                .status(GrievanceStatus.PENDING)
                .build();
        grievanceRepository.save(grievance);
    }
    @Override
    public List<Grievance> getPendingGrievances() {
        return grievanceRepository.findAllByStatus(GrievanceStatus.PENDING);
    }

    @Override
    public void resolveGrievance(Long grievanceId, UserEntity currentUser) {
        Grievance grievance = grievanceRepository.findById(grievanceId).orElseThrow(()-> new RuntimeException("exception not found"));
        grievance.setStatus(GrievanceStatus.RESOLVED);
        // Locale date time
        LocalDateTime  dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // send message to user that grievance was resolved
        Chat chat =   chatService.findOrCreateChat(currentUser , grievance.getComplainant());
        Message message = Message.builder()
                .author(currentUser.getUsername())
                .type(MessageType.CHAT)
                .text("Your grievance "+ grievance.getId() +" was successfully resolved")
                .user(currentUser)
                .chat(chat)
                .pubDate(dateTime.format(formatter))
                .build();
        messageService.saveMessage(message,chat.getId(),currentUser);
        grievanceRepository.save(grievance);
    }
    @Override
    public void dismissGrievance(Long grievanceId, UserEntity currentUser) {
        Grievance grievance = grievanceRepository.findById(grievanceId).orElseThrow(()-> new RuntimeException("exception not found"));
        grievance.setStatus(GrievanceStatus.DISMISSED);
        // Locale date time
        LocalDateTime  dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // send message to user that grievance was resolved
        Chat chat =   chatService.findOrCreateChat(currentUser , grievance.getComplainant());
        Message message = Message.builder()
                .author(currentUser.getUsername())
                .type(MessageType.CHAT)
                .text("Your grievance № "+grievance.getId()+ " was dismissed")
                .user(currentUser)
                .chat(chat)
                .pubDate(dateTime.format(formatter))
                .build();
        messageService.saveMessage(message,chat.getId(),currentUser);
        grievanceRepository.save(grievance);
    }
@Override
public boolean hasPendingGrievance(UserEntity complainant, UserEntity reportedUser) {
    return grievanceRepository.existsByComplainantAndReportedUserAndStatus(
            complainant, reportedUser, GrievanceStatus.PENDING);
}


}
