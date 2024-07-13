package com.example.bookingproject.Repository;

import com.example.bookingproject.Model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository  extends JpaRepository<Comment, Long> {

}
