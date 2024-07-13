package com.example.bookingproject.Repository;

import com.example.bookingproject.Model.BookingEntity;
import com.example.bookingproject.Model.Rating;
import com.example.bookingproject.Model.Security.UserEntity;
import org.hibernate.boot.archive.internal.JarProtocolArchiveDescriptor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.nio.file.LinkOption;

public interface RattingRepository extends JpaRepository<Rating, Long> {
    Rating findByBookingAndUser(BookingEntity booking, UserEntity user);
}
