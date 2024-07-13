package com.example.bookingproject.Controller;

import com.example.bookingproject.Model.Comment;
import com.example.bookingproject.Model.Security.UserEntity;
import com.example.bookingproject.Security.SecurityUtil;
import com.example.bookingproject.Service.CommentService;
import com.example.bookingproject.Service.Security.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/comments")
@Slf4j
public class CommentController {
    private UserService userService;
    private CommentService commentService;

    public CommentController(UserService userService,CommentService commentService) {
        this.userService = userService;
        this.commentService= commentService;
    }

    @PostMapping("/bookings/{bookingId}/save")
    public String saveComment(@ModelAttribute("comment") Comment comment, @PathVariable("bookingId") Long bookingId, RedirectAttributes redirectAttributes)
    {
        log.info("Save comment controller method is working");
        UserEntity user = userService.findByUsername(SecurityUtil.getSessionUser());
        if(user == null)
        {
            redirectAttributes.addFlashAttribute("loginError", "You must be logged in");
            return "redirect:/login";
        }
        Comment savedComment = commentService.saveComment(comment, bookingId,user.getId());
        if(savedComment == null)
            log.error("in \"save comment \" logic was error ");
        else
            log.info("Comment successfully saved");

        return "redirect:/bookings/"+bookingId;
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
    @PostMapping("/{commentId}/action")
    public String commentActionLogic(@PathVariable("commentId") Long commentId, @RequestParam(value = "interaction") String interaction,
                                     RedirectAttributes redirectAttributes)
    {
        UserEntity user = userService.findByUsername(SecurityUtil.getSessionUser());
        Comment review = commentService.findById(commentId);
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
