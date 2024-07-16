package com.example.bookingproject.Controller;

import com.example.bookingproject.Config.BookingType;
import com.example.bookingproject.Dto.BookingPagination;
import com.example.bookingproject.Dto.BookingRequestDto;
import com.example.bookingproject.Model.BookingEntity;
import com.example.bookingproject.Model.Chat;
import com.example.bookingproject.Model.Security.UserEntity;
import com.example.bookingproject.Security.SecurityUtil;
import com.example.bookingproject.Service.BookingService;
import com.example.bookingproject.Service.ChatService;
import com.example.bookingproject.Service.Security.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;

@Controller
@Slf4j
public class MainController {
    private BookingService bookingService;
    private UserService userService;
    private ChatService chatService;
    @Autowired
    public MainController(BookingService bookingService,UserService userService,ChatService chatService) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.chatService = chatService;
    }

    @GetMapping("/home")
    public String mainPage(Model model,
                           @RequestParam(value="pageNo", defaultValue="0",required=false) int pageNo,
                           @RequestParam(value="pageSize", defaultValue="12",required=false) int pageSize)
    {
        BookingPagination availableBookings = bookingService.getAllAvailableBooking(pageNo,pageSize);
        log.error("User name: "+ SecurityUtil.getSessionUser());
        if(SecurityUtil.getSessionUser() != null)
            log.error("User company name: "+ userService.findByUsername(SecurityUtil.getSessionUser()).getCompanyName());
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
                            @RequestParam(value = "companyName",required = false) String companyName,
                            // for  pagination
                            @RequestParam(value="pageNo", defaultValue="0",required=false) int pageNo,
                            @RequestParam(value="pageSize", defaultValue="12",required=false) int pageSize
    )
    {
        BookingPagination bookings = bookingService.findBookingsByParameters(bookingType,occupied,country,city,address,query,sort,companyName,pageNo,pageSize);
        bookings.getData().forEach(bookingEntity -> System.out.println(bookingEntity.getTitle()));
        model.addAttribute("bookings", bookings);
        return "mainPage";
    }
    @GetMapping("/bookings/{bookingId}")
    public String getBookingDetailPage(@PathVariable("bookingId") Long bookingId,
                                       Model model) {
        BookingEntity bookingEntity = bookingService.findById(bookingId);
        UserEntity user = userService.findByUsername(SecurityUtil.getSessionUser());
        List<BookingEntity> companyBookingEntities = null;
        if (bookingEntity == null)
            return "redirect:/home?operationError";
        if (bookingEntity.getCompanyName() != null){
            companyBookingEntities = bookingService.findBookingsByCompanyName(bookingEntity.getCompanyName());
            companyBookingEntities.remove(bookingEntity);
         }

        System.out.println(bookingEntity.getRating());
        model.addAttribute("booking",bookingEntity);
        model.addAttribute("companyBookingEntities",companyBookingEntities);
        model.addAttribute("user",user);
        return "detailPage";
    }
    @GetMapping("/configuration")
    public String getConfigurationPage(Model model, RedirectAttributes redirectAttributes) {
        UserEntity user = userService.findByUsername(SecurityUtil.getSessionUser());
        if (user == null) {
            redirectAttributes.addFlashAttribute("loginError", "You must be logged in");
            return "redirect:/login";
        }

        // For Bookings Creator
        if (user.getAuthoredBookings().size() > 0) {
            List<BookingRequestDto> bookingRequestsList = userService.findUserBookingsWithRequests(user);
            model.addAttribute("bookingRequests", bookingRequestsList);
            if(bookingRequestsList == null)
                log.error("Error is null");
            if(bookingRequestsList.isEmpty())
                log.error("Error is empty");
            bookingRequestsList.forEach(System.out::println);
            List<Chat> userExistingChats = chatService.findChatByUser(user);
            model.addAttribute("userExistingChats", userExistingChats);
            bookingRequestsList.forEach(System.out::println);
            List<BookingRequestDto> bookingConfirmedList = userService.findUserConfirmsBookings(user);
            model.addAttribute("bookingConfirmed", bookingConfirmedList);
            bookingRequestsList.forEach(System.out::println);
        }
        // For Admin
        if (user.hasAdminRole())
        {
            log.info("user has admin role");
            List<UserEntity> bookingsCreatorsList = userService.findAllBookingsCreators();
            bookingsCreatorsList.remove(user);
            model.addAttribute("bookingsCreators", bookingsCreatorsList);

            List<UserEntity> allUserLists = userService.findAllUsers();
            allUserLists.remove(user);
            model.addAttribute("allUsers", allUserLists);

            List<BookingEntity> bookingEntities = bookingService.findAllBookings();
            model.addAttribute("bookingsEntities", bookingEntities);
        }
        // For all users
        Set<BookingEntity> userConfirmedBooking = user.getConfirmedBookings();
        Set<BookingEntity> userBookingRequest= user.getRequestingBookings();

        model.addAttribute("userConfirmedBooks",userConfirmedBooking.stream().toList());
        model.addAttribute("userBookingRequest",userBookingRequest.stream().toList());
        model.addAttribute("currentUser", user);
     return "configurationPage";
    }
}
