package com.example.bookingproject.Repository;

import com.example.bookingproject.Model.Attachment;
import com.example.bookingproject.Model.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AttachmentRepository  extends JpaRepository<Attachment,Long> {
    List<Attachment>  findAllByBooking(BookingEntity booking);
}
