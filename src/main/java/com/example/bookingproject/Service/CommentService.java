package com.example.bookingproject.Service;

import com.example.bookingproject.Model.Comment;

public interface CommentService {
    String delete(Comment comment);

    Comment saveComment(Comment comment, Long bookingId, Long userId);

    Comment findById(Long commentId);
}
