package com.example.bookingproject.Service.impl;

import com.example.bookingproject.Model.BookingEntity;
import com.example.bookingproject.Model.Comment;
import com.example.bookingproject.Model.Security.UserEntity;
import com.example.bookingproject.Repository.CommentRepository;
import com.example.bookingproject.Security.SecurityUtil;
import com.example.bookingproject.Service.BookingService;
import com.example.bookingproject.Service.CommentService;
import com.example.bookingproject.Service.Security.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CommentServiceimpl implements CommentService {
    @Lazy
    private UserService userService;
    private BookingService bookingService;
    private CommentRepository commentRepository;

    @Autowired
    public CommentServiceimpl(UserService userService, BookingService bookingService, CommentRepository commentRepository) {
        this.userService = userService;
        this.bookingService = bookingService;
        this.commentRepository = commentRepository;
    }
    @Transactional
    @Override
    public Comment saveComment(Comment comment, Long bookingId, Long userId) {
        UserEntity user = userService.findByUsername(SecurityUtil.getSessionUser());
        BookingEntity bookingEntity =  bookingService.findById(bookingId);
        comment.setUser(user);
        comment.setBooking(bookingEntity);
        // Comment publish time
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        comment.setPubDate(formattedDateTime);
        commentRepository.save(comment);
        bookingService.updateBookingRating(bookingEntity); // update booking rating
        return comment;
    }

    @Override
    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId).get();
    }

    @Override
    public String delete(Comment comment) {
        List<UserEntity> usersWhoLiked = userService.findAllByLikedComments(comment);
        List<UserEntity> usersWhoDisLiked= userService.findAllByDislikedComments(comment);
        if(usersWhoLiked != null || usersWhoDisLiked != null) {
            for (UserEntity user : usersWhoLiked) {
                user.getLikedComments().remove(comment);
                userService.save(user);
            }
            for (UserEntity user : usersWhoDisLiked) {
                user.getDislikedComments().remove(comment);
                userService.save(user);
            }
        }
        commentRepository.delete(comment);
        return "success";
    }
}
