package com.example.bookingproject.Controller;

import com.example.bookingproject.Model.BookingEntity;
import com.example.bookingproject.Model.Security.UserEntity;
import com.example.bookingproject.Security.SecurityUtil;
import com.example.bookingproject.Service.BookingService;
import com.example.bookingproject.Service.Security.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Controller
@Slf4j
@RequestMapping("/configuration")
@RestController
public class ConfigurationController {
    private BookingService bookingService;
    private UserService userService;

    @Autowired
    public ConfigurationController(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }
    // Confirm users request
    @Transactional
    @PostMapping("/confirmRequest")
    public ResponseEntity<?> confirmBookingRequest(@RequestParam("bookingTitle") String bookingTitle,
                                                   @RequestParam("username") String username)
    {

        BookingEntity bookingEntity = bookingService.findByTitle(bookingTitle);
        UserEntity user = userService.findByUsername(username);


        if (bookingEntity == null || user == null || bookingEntity.getOccupied() == true ) {
            bookingService.redirect("/home","operationError");
        }

        if (!bookingEntity.getRequestingUsers().contains(user)) {
            bookingService.redirect("/home","operationError");
        }

        bookingEntity.removeRequestingUser(user);
        bookingEntity.addConfirmedUser(user);
        System.out.println(bookingEntity.getConfirmedUsers().size());
        if(bookingEntity.getConfirmedUsers().size() == bookingEntity.getCapacity())
        {
            bookingEntity.setOccupied(true);
        }

        bookingService.save(bookingEntity);

        log.info("Booking confirmed for user {} and booking {}", user.getUsername(), bookingEntity.getId());

        String redirectUrl = "/configuration";
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(redirectUrl)
                .build()
                .toUri();
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(location)
                .build();
    }
    // Delete users request
    @Transactional
    @PostMapping("/rejectRequest")
    public ResponseEntity<?> rejectBookingRequest(@RequestParam("bookingTitle") String bookingTitle,
                                                  @RequestParam("username") String username)
    {

        BookingEntity bookingEntity = bookingService.findByTitle(bookingTitle);
        UserEntity user = userService.findByUsername(username);


        if (bookingEntity == null || user == null) {
            bookingService.redirect("/home","operationError");
        }

        if (!bookingEntity.getRequestingUsers().contains(user)) {
            bookingService.redirect("/home","operationError");
        }

        bookingEntity.removeRequestingUser(user);
        bookingService.save(bookingEntity);

        log.info("Booking confirmed for user {} and booking {}", user.getUsername(), bookingEntity.getId());

        String redirectUrl = "/configuration";
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(redirectUrl)
                .build()
                .toUri();
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(location)
                .build();
    }
    // Delete confirmed user
    @Transactional
    @PostMapping("/deleteConfirm")
    public ResponseEntity<?> rejectBookingConfirmed(@RequestParam("bookingTitle") String bookingTitle,
                                                    @RequestParam("username") String username)
    {
        BookingEntity bookingEntity = bookingService.findByTitle(bookingTitle);
        UserEntity user = userService.findByUsername(username);


        if (bookingEntity == null || user == null) {
            bookingService.redirect("/home","operationError");
        }

        if (bookingEntity.getRequestingUsers().contains(user)) {
            bookingService.redirect("/home","operationError");
        }

        bookingEntity.removeConfirmedUser(user);

        if(bookingEntity.getConfirmedUsers().size() < bookingEntity.getCapacity())
        {
            bookingEntity.setOccupied(false);
        }
        bookingService.save(bookingEntity);

        log.info("Booking confirmed for user {} and booking {}", user.getUsername(), bookingEntity.getId());

        String redirectUrl = "/configuration";
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(redirectUrl)
                .build()
                .toUri();
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(location)
                .build();
    }

    // verify admin logic
    @PostMapping("users/verify")
    public ResponseEntity<?> verifyUser(@RequestParam("userId") Long userId)
    {
        UserEntity currentUser = userService.findByUsername(SecurityUtil.getSessionUser());

        if(currentUser == null || !currentUser.hasAdminRole())
        {
         bookingService.redirect("/home","operationError");
        }
        userService.verifyUser(userId);

        return  bookingService.redirect("/configuration",null);
    }
}
