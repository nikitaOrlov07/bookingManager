package com.example.bookingproject.Controller;

import com.example.bookingproject.Model.BookingEntity;
import com.example.bookingproject.Model.Security.UserEntity;
import com.example.bookingproject.Security.SecurityUtil;
import com.example.bookingproject.Service.BookingService;
import com.example.bookingproject.Service.Security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/bookings")
public class RatingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;
/*
    @PostMapping("/{bookingId}/rate")
    public ResponseEntity<Map<String, Object>> rateBooking(@PathVariable Long bookingId, @RequestBody Map<String, Integer> ratingData) {
        int rating = ratingData.get("rating");
        UserEntity user = userService.findByUsername(SecurityUtil.getSessionUser());
        BookingEntity booking = bookingService.findById(bookingId);

        if (booking == null) {
            return ResponseEntity.notFound().build();
        }


    }

 */
}