package com.example.bookingproject.Controller;

import com.example.bookingproject.Model.Attachment;
import com.example.bookingproject.Model.BookingEntity;
import com.example.bookingproject.Model.Security.UserEntity;
import com.example.bookingproject.Security.SecurityUtil;
import com.example.bookingproject.Service.AttachmentService;
import com.example.bookingproject.Service.BookingService;
import com.example.bookingproject.Service.Security.UserService;
import com.example.bookingproject.Service.impl.AttachmentServiceimpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Controller
@Slf4j
@RequestMapping("/files")
public class AttachmentController {
    private AttachmentService attachmentService;
    private BookingService bookingService;
    private UserService userService;

    @Autowired
    public AttachmentController(AttachmentService attachmentService, BookingService bookingService, UserService userService) {
        this.attachmentService = attachmentService;
        this.bookingService = bookingService;
        this.userService = userService;
    }

    // download file
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") Long fileId) throws Exception // method will return file content and file metadata
    {
        Attachment attachment = attachmentService.getAttachment(fileId);
        UserEntity currentUser = userService.findByUsername(SecurityUtil.getSessionUser());
        BookingEntity booking = attachment.getBooking();
        if(currentUser == null)
        {
          attachmentService.opperationError();
        }
        return ResponseEntity.ok() // request was successful
                .contentType(MediaType.parseMediaType(attachment.getFileType())) // This sets the Content-Type of the file using the fileType stored in the attachment object. MediaType.parseMediaType() converts the string representation of the file type into a MediaType object.
                .header(HttpHeaders.CONTENT_DISPOSITION ,
                        "attachment;filename=\"" + attachment.getFileName()+"\"") // Sets the Content-Disposition header. The value "attachment;filename=\"" indicates to the browser that the file should be displayed as an attachment and not opened in the browser. attachment.getFileName() is used to set the file name.
                .body( new ByteArrayResource(attachment.getData())); // we return the contents of the file as a resource of type ByteArrayResource. This allows Spring to treat the byte array as a data stream for the HTTP response.
    }
    // Display file contents
    @GetMapping("/view/{fileId}")
    public ResponseEntity<Resource> viewFile(@PathVariable("fileId") Long fileId) throws Exception {
        UserEntity currentUser = userService.findByUsername(SecurityUtil.getSessionUser());
        BookingEntity booking = attachmentService.getAttachment(fileId).getBooking();
        if(currentUser == null)
        {
            attachmentService.opperationError();
        }
        Attachment attachment = attachmentService.getAttachment(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType())) // ->  converts a string representation of a file type to a MediaType object
                .body(new ByteArrayResource(attachment.getData()));
    }
}


