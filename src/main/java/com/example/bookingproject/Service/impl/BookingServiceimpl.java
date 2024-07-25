package com.example.bookingproject.Service.impl;

import com.example.bookingproject.Config.BookingType;
import com.example.bookingproject.Dto.BookingDto;
import com.example.bookingproject.Dto.BookingPagination;
import com.example.bookingproject.Mapper.BookingMapper;
import com.example.bookingproject.Model.Attachment;
import com.example.bookingproject.Model.BookingEntity;
import com.example.bookingproject.Model.Comment;
import com.example.bookingproject.Model.Security.UserEntity;
import com.example.bookingproject.Repository.BookingEntityRepository;
import com.example.bookingproject.Security.SecurityUtil;
import com.example.bookingproject.Service.AttachmentService;
import com.example.bookingproject.Service.BookingService;
import com.example.bookingproject.Service.CommentService;
import com.example.bookingproject.Service.Security.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class BookingServiceimpl implements BookingService {
    @Autowired
    private BookingEntityRepository bookingRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Lazy
    @Autowired
    private AttachmentService attachmentService;

    Page<BookingEntity> bookingPage =null;
    @Transactional
    @Override
    public BookingPagination getAllAvailableBooking(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo,pageSize);
        bookingPage = bookingRepository.findAll(pageable);
        List<BookingEntity> bookingList = bookingPage.getContent();
        BookingPagination bookingPagination = BookingPagination.builder()
                .data(bookingList)
                .pageNo(bookingPage.getNumber())
                .pageSize(bookingPage.getSize())
                .totalElements(bookingPage.getTotalElements())
                .totalPages(bookingPage.getTotalPages())
                .last(bookingPage.isLast())
                .build();
        return bookingPagination;
    }
    @Transactional
    @Override
    public BookingPagination findBookingsByParameters(BookingType bookingType, Boolean occupied, String country, String city, String address, String title, String sort, String companyName, int pageNo, int pageSize) {
        Sort sort_object = Sort.unsorted(); // by rating , by bookingsize
        if(sort!=null && !sort.isEmpty())
        {
            if(sort.equals("capacity"))
                sort_object=Sort.by(Sort.Direction.DESC,"capacity");
            else if(sort.equals("price"))
                sort_object=Sort.by(Sort.Direction.DESC,"price");
            else if(sort.equals("rating"))
                sort_object=Sort.by(Sort.Direction.DESC,"rating");

        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort_object);

        bookingPage= bookingRepository.findByParameters(bookingType, occupied, country,title, city, address,companyName, pageable);

        List<BookingEntity> bookingList = bookingPage.getContent();
        BookingPagination bookingPagination = BookingPagination.builder()
                .data(bookingList)
                .pageNo(bookingPage.getNumber())
                .pageSize(bookingPage.getSize())
                .totalElements(bookingPage.getTotalElements())
                .totalPages(bookingPage.getTotalPages())
                .last(bookingPage.isLast())
                .build();
        return bookingPagination;
    }
    @Transactional
    @Override
    public BookingEntity findById(Long bookingId) {
        return bookingRepository.findById(bookingId).orElse(null);
    }
    @Transactional
    @Override
    public BookingEntity saveBooking(BookingDto bookingDto) {
        BookingEntity bookingEntity = BookingMapper.getBookingEntityFromBookingDto(bookingDto);
        bookingEntity.setOccupied(false);
        return bookingRepository.save(bookingEntity);
    }
    @Transactional
    @Override
    public BookingEntity updateBookings(BookingDto bookingDto) {
        BookingEntity existingBooking = findById(bookingDto.getId());
        if (existingBooking == null) {
            throw new EntityNotFoundException("Booking not found with id: " + bookingDto.getId());
        }

        // Update only fields , that can be changed
        existingBooking.setTitle(bookingDto.getTitle());
        existingBooking.setType(bookingDto.getType());
        existingBooking.setDescription(bookingDto.getDescription());
        // Update amenities
        existingBooking.getAmenities().clear();
        existingBooking.getAmenities().addAll(Arrays.asList(bookingDto.getAmenities().split(",\\s*")));

        // Update conditions
        existingBooking.getConditions().clear();
        existingBooking.getConditions().addAll(Arrays.asList(bookingDto.getConditions().split(",\\s*")));
        existingBooking.setPrice(bookingDto.getPrice());
        existingBooking.setCurrency(bookingDto.getCurrency());
        existingBooking.setBookingTime(bookingDto.getBookingTime());
        existingBooking.setCountry(bookingDto.getCountry());
        existingBooking.setCity(bookingDto.getCity());
        existingBooking.setAddress(bookingDto.getAddress());
        existingBooking.setCapacity(bookingDto.getCapacity());
        existingBooking.setNumberOfRooms(bookingDto.getNumberOfRooms());
        existingBooking.setCompanyName(bookingDto.getCompanyName());

        return bookingRepository.save(existingBooking);
    }

    @Override
    @Transactional
    public void deleteBooking(Long bookingId) throws Exception {
        BookingEntity bookingEntity = findById(bookingId);

        // Also i can use Iterator instead of new Arraylist to create a copy of the collection we are iterating over. This allows us to safely modify the original collection during iteration.
        for (UserEntity user : new ArrayList<>(bookingEntity.getRequestingUsers())) {
            user.getRequestingBookings().remove(bookingEntity);
            userService.save(user);
        }
        bookingEntity.getRequestingUsers().clear();

        for (UserEntity user : new ArrayList<>(bookingEntity.getConfirmedUsers())) {
            user.getConfirmedBookings().remove(bookingEntity);
            userService.save(user);
        }
        bookingEntity.getConfirmedUsers().clear();

        for (Comment review : new ArrayList<>(bookingEntity.getComments())) {
            commentService.delete(review);
        }
        bookingEntity.getComments().clear();

        if (bookingEntity.getAttachments() != null && !bookingEntity.getAttachments().isEmpty()) {
            List<Attachment> attachmentsToRemove = new ArrayList<>(bookingEntity.getAttachments());
            for (Attachment attachment : attachmentsToRemove) {
                attachmentService.delete(attachment.getId());
            }
            bookingEntity.getAttachments().clear();
        }

        bookingEntity.getAmenities().clear();
        bookingEntity.getConditions().clear();

        UserEntity author = bookingEntity.getAuthor();
        author.getAuthoredBookings().remove(bookingEntity);
        userService.save(author);

        bookingRepository.delete(bookingEntity);
    }
    @Transactional
    @Override
    public List<BookingEntity> findBookingsByCompanyName(String companyName) {
        return bookingRepository.findBookingEntitiesByCompanyName(companyName);
    }

    @Transactional
    @Override
    public void uploadFile(MultipartFile file, Long bookingId) throws Exception {
        BookingEntity bookingEntity = findById(bookingId);
        UserEntity user = userService.findByUsername(SecurityUtil.getSessionUser());

        if(user == null || bookingEntity == null) {
            attachmentService.opperationError();
        }
        if(!attachmentService.isValidFileType(file)) {
            attachmentService.opperationError();
        }

        Attachment attachment = attachmentService.saveAttachment(file, bookingEntity, user);

        String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/download/")
                .path(String.valueOf(attachment.getId()))
                .toUriString();
        String viewUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/view/")
                .path(String.valueOf(attachment.getId()))
                .toUriString();

        log.info("Download URL: " + downloadUrl);
        log.info("View URL: " + viewUrl);

        attachmentService.updateAttachmentUrls(attachment.getId(), downloadUrl, viewUrl);
    }

    @Transactional
    @Override
    public List<BookingEntity> findAllBookings() {
        return  bookingRepository.findAllByOrderByIdAsc();
    }

    @Override
    public BookingEntity findByTitle(String bookingTitle) {
        return bookingRepository.findByTitle(bookingTitle).orElse(null);
    }
    @Override
    public BookingEntity save(BookingEntity bookingEntity)
   {
    return bookingRepository.save(bookingEntity);
   }

    @Override
    public ResponseEntity<Object> redirect(String redirectUrl, String param) {
        ServletUriComponentsBuilder builder = (ServletUriComponentsBuilder) ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(redirectUrl);

        if (param != null && !param.isEmpty()) {
            builder.queryParam(param);
        }

        URI location = builder.build().toUri();

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(location)
                .build();
    }
    @Override
    @Transactional
    public void updateBookingRating(BookingEntity booking) {
        booking = bookingRepository.findById(booking.getId())
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        Double averageRating = booking.calculateAverageRating();
        booking.setRating(averageRating);
        bookingRepository.save(booking);
    }

}
