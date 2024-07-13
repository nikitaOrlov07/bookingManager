package com.example.bookingproject.Service.impl;

import com.example.bookingproject.Model.Attachment;
import com.example.bookingproject.Model.BookingEntity;
import com.example.bookingproject.Model.Security.UserEntity;
import com.example.bookingproject.Repository.AttachmentRepository;
import com.example.bookingproject.Service.AttachmentService;
import com.example.bookingproject.Service.BookingService;
import com.example.bookingproject.Service.Security.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AttachmentServiceimpl implements AttachmentService {
    private AttachmentRepository attachmentRepository;
    @Lazy
    private BookingService bookingService;
    private UserService userService;

    @Autowired
    public AttachmentServiceimpl(AttachmentRepository attachmentRepository, BookingService bookingService, UserService userService) {
        this.attachmentRepository = attachmentRepository;
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @Override
    public Attachment saveAttachment(MultipartFile file, BookingEntity bookingEntity, UserEntity user) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if(fileName.contains("..")) {
                throw new Exception("Filename contains invalid path sequence" + fileName);
            }

            Attachment attachment = new Attachment(fileName,
                    file.getContentType(),
                    file.getBytes(),
                    null,
                    null
            );
            attachment.setCreator(user.getUsername());
            attachment.setBooking(bookingEntity);

            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            attachment.setTimestamp(currentDateTime.format(formatter));

            return attachmentRepository.save(attachment);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new Exception("Could not save File: "+fileName);
        }
    }

    @Transactional
    @Override
    public void updateAttachmentUrls(Long id, String downloadUrl, String viewUrl) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Attachment not found"));
        attachment.setDownloadUrl(downloadUrl);
        attachment.setViewUrl(viewUrl);
        attachmentRepository.save(attachment);

    }

    @Override
    public boolean isValidFileType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null) {
            return false;
        }
        return contentType.startsWith("image/") || contentType.startsWith("video/");
    }

    @Override
    public ResponseEntity<Object> opperationError() {
        String redirectUrl = "/home";
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(redirectUrl)
                .queryParam("operationError")
                .build()
                .toUri();
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(location)
                .build();
    }
    @Transactional
    @Override
    public Attachment getAttachment(Long fileId) throws Exception {
        return attachmentRepository.findById(fileId).orElseThrow(()-> new Exception("File not found with id "+ fileId));

    }

    @Override
    @Transactional
    public void delete(Long fileId) throws Exception {
        Attachment attachment = getAttachment(fileId);
        if (attachment != null) {
            BookingEntity booking = attachment.getBooking();
            if (booking != null) {
                booking.getAttachments().remove(attachment);
            }
            attachmentRepository.delete(attachment);
        }
    }




}
