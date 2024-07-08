package com.example.bookingproject.Controller;

import com.example.bookingproject.Model.BookingEntity;
import com.example.bookingproject.Service.BookingEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class MainController {
    private BookingEntityService bookingService;

    @Autowired
    public MainController(BookingEntityService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/home")
    public String mainPage(Model model)
    {
        List<BookingEntity> availableBookings = bookingService.getAllAvailableBooking();
        model.addAttribute("availableBookings", availableBookings);
        return "mainPage";
    }
}
