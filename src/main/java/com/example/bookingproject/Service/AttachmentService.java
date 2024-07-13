package com.example.bookingproject.Service;

import com.example.bookingproject.Model.Attachment;
import com.example.bookingproject.Model.BookingEntity;
import com.example.bookingproject.Model.Security.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {

    Attachment saveAttachment(MultipartFile file, BookingEntity bookingEntity, UserEntity user) throws Exception;

    void updateAttachmentUrls(Long id, String downloadUrl, String viewUrl);

    boolean isValidFileType(MultipartFile file);

    ResponseEntity<Object> opperationError();

    Attachment getAttachment(Long fileId) throws Exception;

    @Transactional // operations with large objects must be transactional
    void delete(Long projectId) throws Exception;
}
