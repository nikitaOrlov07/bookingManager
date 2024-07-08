package com.example.bookingproject.Repository;

import com.example.bookingproject.Model.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingEntityRepository extends JpaRepository<BookingEntity,Long> {
    List<BookingEntity> getBookEntitiesByOccupiedFalse();
}
