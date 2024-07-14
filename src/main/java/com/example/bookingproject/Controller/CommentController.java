package com.example.bookingproject.Controller;

import com.example.bookingproject.Model.BookingEntity;
import com.example.bookingproject.Model.Comment;
import com.example.bookingproject.Model.Security.UserEntity;
import com.example.bookingproject.Security.SecurityUtil;
import com.example.bookingproject.Service.BookingService;
import com.example.bookingproject.Service.CommentService;
import com.example.bookingproject.Service.Security.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/reviews")
@Slf4j
public class CommentController {
    private UserService userService;
    private CommentService commentService;
    private BookingService bookingService;

    public CommentController(UserService userService,CommentService commentService, BookingService bookingService) {
        this.userService = userService;
        this.commentService= commentService;
        this.bookingService = bookingService;
    }

    @PostMapping("/bookings/{bookingId}/save")
    public String saveReview(@PathVariable Long bookingId, @RequestParam String text, @RequestParam int rating, Principal principal) {
        BookingEntity booking = bookingService.findById(bookingId);
        UserEntity user = userService.findByUsername(principal.getName());

        Comment comment = Comment.builder()
                .text(text)
                .rating(rating)
                .booking(booking)
                .user(user)
                .pubDate(LocalDateTime.now().toString())
                .build();

        commentService.saveComment(comment,booking.getId(),user.getId());

        return "redirect:/bookings/" + bookingId;
    }
    @PostMapping("/delete/{commentId}")
    public String deleteComment(@PathVariable("commentId") Long commentId,RedirectAttributes redirectAttributes)
    {
        UserEntity user = userService.findByUsername(SecurityUtil.getSessionUser());
        Comment review = commentService.findById(commentId);
        if(user == null)
        {
            redirectAttributes.addFlashAttribute("loginError", "You must be logged in");
            return "redirect:/login";
        }
        if(review == null ||  (!review.getUser().equals(user) && !review.getBooking().getAuthor().equals(user)))
        {
            return "redirect:/home?operationError";
        }
        String result = commentService.delete(review);
        if(result == "success")
            log.info("\"Comment Deletion logic\" is working");
        else
            log.error("in \"Comment Deletion logic\" was error");
        return "redirect:/bookings/"+ review.getBooking().getId();
    }
    @PostMapping("/{reviewId}/action")
    public String commentActionLogic(@PathVariable("reviewId") Long reviewId, @RequestParam(value = "interaction") String interaction,
                                     RedirectAttributes redirectAttributes)
    {
        UserEntity user = userService.findByUsername(SecurityUtil.getSessionUser());
        Comment review = commentService.findById(reviewId);
        if(user == null)
        {
            redirectAttributes.addFlashAttribute("loginError", "You must be logged in");
            return "redirect:/login";
        }
        if(review == null)
        {
            return "redirect:/home?operationError";
        }
        if(interaction != null)
        {
            switch (interaction) {
                case "like":
                    userService.actionComment("like", review);
                    break;
                case "dislike":
                    userService.actionComment("dislike", review);
                    break;
            }
        }
     else {
        log.error("Interaction variable is null");
    }
    return  "redirect:/bookings/"+ review.getBooking().getId();
}
}
