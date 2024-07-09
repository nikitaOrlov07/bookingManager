package com.example.bookingproject.Controller;

import com.example.bookingproject.Config.BookingType;
import com.example.bookingproject.Dto.BookingPagination;
import com.example.bookingproject.Model.BookingEntity;
import com.example.bookingproject.Service.BookingEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        BookingPagination availableBookings = bookingService.getAllAvailableBooking();
        model.addAttribute("availableBookings", availableBookings);
        return "mainPage";
    }
    @GetMapping("/home/find")
    public String findLogic(Model model,
                            @RequestParam(value="type", required=false) BookingType bookingType,
                            @RequestParam(value="occupied",required = false) Boolean occupied ,
                            @RequestParam(value="country",required= false) String country,
                            @RequestParam(value ="city",required = false) String city ,
                            @RequestParam(value = "address",required = false) String address,
                            @RequestParam(value="query",required = false) String query,
                            @RequestParam(value = "sort",required = false) String sort,
                            // for  pagination
                            @RequestParam(value="pageNo", defaultValue="0",required=false) int pageNo,
                            @RequestParam(value="pageSize", defaultValue="12",required=false) int pageSize
    )
    {
        BookingPagination bookings = bookingService.findBookingsByParameters(bookingType,occupied,country,city,address,query,sort,pageNo,pageSize);
        model.addAttribute("bookings", bookings);
        return "mainPage";
    }
}
