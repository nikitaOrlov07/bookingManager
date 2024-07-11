package com.example.bookingproject.Controller;

import com.example.bookingproject.Config.BookingType;
import com.example.bookingproject.Dto.BookingPagination;
import com.example.bookingproject.Model.BookingEntity;
import com.example.bookingproject.Model.Security.UserEntity;
import com.example.bookingproject.Security.SecurityUtil;
import com.example.bookingproject.Service.BookingService;
import com.example.bookingproject.Service.Security.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class MainController {
    private BookingService bookingService;
    private UserService userService;
    @Autowired
    public MainController(BookingService bookingService,UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @GetMapping("/home")
    public String mainPage(Model model,
                           @RequestParam(value="pageNo", defaultValue="0",required=false) int pageNo,
                           @RequestParam(value="pageSize", defaultValue="12",required=false) int pageSize)
    {
        BookingPagination availableBookings = bookingService.getAllAvailableBooking(pageNo,pageSize);
        log.error("User name: "+ SecurityUtil.getSessionUser());
        model.addAttribute("bookings", availableBookings);
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
        bookings.getData().forEach(bookingEntity -> System.out.println(bookingEntity.getTitle()));
        model.addAttribute("bookings", bookings);
        return "mainPage";
    }
    @GetMapping("/bookings/{bookingId}")
    public String getBookingDetailPage(@PathVariable("bookingId") Long bookingId,
                                       Model model)
    {
        BookingEntity bookingEntity = bookingService.findById(bookingId);
        UserEntity user = userService.findByUsername(SecurityUtil.getSessionUser());
        if(bookingEntity == null)
            return "redirect:/home?operationError";
        model.addAttribute("booking",bookingEntity);
        model.addAttribute("user",user);
        return "detailPage";
    }
}
