package com.example.bookingproject.Controller;

import com.example.bookingproject.Dto.BookingDto;
import com.example.bookingproject.Mapper.BookingMapper;
import com.example.bookingproject.Model.BookingEntity;
import com.example.bookingproject.Model.Security.UserEntity;
import com.example.bookingproject.Security.SecurityUtil;
import com.example.bookingproject.Service.AttachmentService;
import com.example.bookingproject.Service.BookingService;
import com.example.bookingproject.Service.Security.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@Controller
@RequestMapping("/bookings")
@Slf4j
public class BookingController {
    private UserService userService; private BookingService bookingsService; private AttachmentService attachmentService;

    @Autowired
    public BookingController(UserService userService , BookingService bookingsService ,AttachmentService attachmentService){
        this.userService = userService;
        this.bookingsService = bookingsService;
        this.attachmentService = attachmentService;
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
                              @RequestParam("files") MultipartFile[] files,
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
        bookingDto.setCompanyName(user.getCompanyName());
        BookingEntity savedBooking = bookingsService.saveBooking(bookingDto);

        // file saving
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    bookingsService.uploadFile(file, savedBooking.getId());
                } catch (Exception e) {
                    log.error("Error uploading file", e);
                }
            }
        }
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
                                 @RequestParam(value = "newFiles", required = false) MultipartFile[] newFiles,
                                 @RequestParam(value = "deletedFileIds", required = false) String deletedFileIds,
                                 RedirectAttributes redirectAttributes,
                                 Model model) throws  Exception
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
        // Work with files

        // Delete delete files
        if (deletedFileIds != null && !deletedFileIds.isEmpty()) {
            Arrays.stream(deletedFileIds.split(","))
                    .map(Long::parseLong)
                    .forEach(fileId -> {
                        try {
                            attachmentService.delete(fileId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        }

        // Add Added files
        if (newFiles != null) {
            for (MultipartFile file : newFiles) {
                if (!file.isEmpty()) {
                    try {
                        attachmentService.saveAttachment(file, savedBookingEntity, bookingDto.getAuthor());
                    } catch (Exception e) {
                       e.printStackTrace();
                    }
                }
            }
        }
        return "redirect:/bookings/"+savedBookingEntity.getId()+"?successfullyUpdated";
    }
    // Delete Booking
    @PostMapping("/delete/{bookingId}")
    public String deleteBooking(@PathVariable("bookingId") Long bookingId,
                                RedirectAttributes redirectAttributes) throws Exception {
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