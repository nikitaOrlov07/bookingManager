package com.example.bookingproject.Controller;

import com.example.bookingproject.Model.Security.UserEntity;
import com.example.bookingproject.Repository.GrievanceRepository;
import com.example.bookingproject.Security.SecurityUtil;
import com.example.bookingproject.Service.GrievanceService;
import com.example.bookingproject.Service.Security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/grievance")
public class GrievanceController {
    private UserService userService; private GrievanceService grievanceService;

    @Autowired
    public GrievanceController(UserService userService , GrievanceService grievanceService) {
        this.userService = userService;
        this.grievanceService = grievanceService;
    }


    @PostMapping("/submit")
    public String addGrievence(@RequestParam("reportedUserId") Long reportedUserId,
                               @RequestParam("text") String text,
                               RedirectAttributes redirectAttributes)
    {
        UserEntity currentUser = userService.findByUsername(SecurityUtil.getSessionUser());
        if(currentUser == null)
        {
            redirectAttributes.addFlashAttribute("loginError", "You must be logged in");
            return "redirect:/login";
        }
        grievanceService.createGrievence(currentUser,reportedUserId, text);
        return "redirect:/configuration";
    }
    @PostMapping("/resolve/{grievanceId}")
    public String resolveGrievance(@PathVariable("grievanceId") Long grievanceId ,
                                   RedirectAttributes redirectAttributes)
    {
        UserEntity currentUser = userService.findByUsername(SecurityUtil.getSessionUser());
        if(currentUser == null || !currentUser.hasAdminRole())
        {
            redirectAttributes.addFlashAttribute("OperationError");
            return "redirect:/home";
        }
        grievanceService.resolveGrievance(grievanceId,currentUser);
        return "redirect:/configuration";
    }
    @PostMapping("/dismiss/{grievanceId}")
    public String dismissGrievance(@PathVariable("grievanceId") Long grievanceId,
                                   RedirectAttributes redirectAttributes)
    {
        UserEntity currentUser = userService.findByUsername(SecurityUtil.getSessionUser());
        if(currentUser == null || !currentUser.hasAdminRole())
        {
            redirectAttributes.addFlashAttribute("OperationError");
            return "redirect:/home";
        }
        grievanceService.dismissGrievance(grievanceId,currentUser);
        return "redirect:/configuration";
    }

}
