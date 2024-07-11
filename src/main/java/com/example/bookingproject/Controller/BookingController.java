package com.example.bookingproject.Controller;

import com.example.bookingproject.Dto.BookingDto;
import com.example.bookingproject.Mapper.BookingMapper;
import com.example.bookingproject.Model.BookingEntity;
import com.example.bookingproject.Model.Security.UserEntity;
import com.example.bookingproject.Security.SecurityUtil;
import com.example.bookingproject.Service.BookingService;
import com.example.bookingproject.Service.Security.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/bookings")
@Slf4j
public class BookingController {
    private UserService userService; private BookingService bookingsService;

    @Autowired
    public BookingController(UserService userService , BookingService bookingsService){
        this.userService = userService;
        this.bookingsService = bookingsService;
    }

    // create bookingEntity
    @GetMapping("/create")
    public String createBooking(RedirectAttributes redirectAttributes,
                                Model model)
    {
        UserEntity user = userService.findByUsername(SecurityUtil.getSessionUser());
        if(user == null)
        {
            redirectAttributes.addFlashAttribute("loginError", "You must be logged in");
            return "redirect:/login";
        }
        BookingDto bookingDto = new BookingDto();
        model.addAttribute("bookingDto",bookingDto);
        return "createBooking";
    }
    @PostMapping("/create/save")
    public String saveBooking(@ModelAttribute("bookingDto") @Valid BookingDto bookingDto,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Model model)
    {
        UserEntity user = userService.findByUsername(SecurityUtil.getSessionUser());
        if(user == null)
        {
            redirectAttributes.addFlashAttribute("loginError", "You must be logged in");
            return "redirect:/login";
        }
        if(result.hasErrors())
        {
         model.addAttribute("bookingDto",bookingDto);
         return "createBooking";
        }
        log.info("save for create is working");
        bookingDto.setAuthor(user);
        BookingEntity savedBooking = bookingsService.saveBooking(bookingDto);
        return "redirect:/bookings/"+ savedBooking.getId()+"?bookingSuccessfullyCreate";
    }

    // update booking entity
    @GetMapping("/update/{bookingId}")
    public String updateBooking(@PathVariable("bookingId") Long bookingId,
                                RedirectAttributes redirectAttributes,
                                Model model)
    {
        BookingEntity bookingEntity = bookingsService.findById(bookingId);
        UserEntity user = userService.findByUsername(SecurityUtil.getSessionUser());
        if(user == null)
        {
            redirectAttributes.addFlashAttribute("loginError", "You must be logged in");
            return "redirect:/login";
        }
        if(bookingEntity == null && !bookingEntity.getAuthor().equals(user))
            return "redirect:/home?operationError";

        model.addAttribute("bookingDto", BookingMapper.getBookingDtoFromBookingEntity(bookingEntity));
        return "updateBooking";
    }
    @PostMapping("/update/save")
    public String updateBookings(@ModelAttribute("bookingDto") @Valid BookingDto bookingDto,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model)
    {
        UserEntity user = userService.findByUsername(SecurityUtil.getSessionUser());
        if(user == null)
        {
            redirectAttributes.addFlashAttribute("loginError", "You must be logged in");
            return "redirect:/login";
        }
        if(result.hasErrors())
        {
            model.addAttribute("bookingDto",bookingDto);
            return "updateBooking";
        }
        BookingEntity savedBookingEntity = bookingsService.updateBookings(bookingDto);
        return "redirect:/bookings/"+savedBookingEntity.getId()+"?successfullyUpdated";
    }
    // Delete Booking
    @PostMapping("/delete/{bookingId}")
    public String deleteBooking(@PathVariable("bookingId") Long bookingId,
                                RedirectAttributes redirectAttributes)
    {
        UserEntity user = userService.findByUsername(SecurityUtil.getSessionUser());
        BookingEntity bookingEntity = bookingsService.findById(bookingId);
        if(user == null)
        {
            redirectAttributes.addFlashAttribute("loginError", "You must be logged in");
            return "redirect:/login";
        }
        if(bookingEntity == null && !bookingEntity.getAuthor().equals(user))
            return "redirect:/home?operationError";

        bookingsService.deleteBooking(bookingEntity);
        return "redirect:/home?successDeletedBooking";
    }

}
