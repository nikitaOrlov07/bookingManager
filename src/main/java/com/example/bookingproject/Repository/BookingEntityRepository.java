package com.example.bookingproject.Repository;

import com.example.bookingproject.Config.BookingType;
import com.example.bookingproject.Dto.BookingPagination;
import com.example.bookingproject.Model.BookingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookingEntityRepository extends JpaRepository<BookingEntity,Long> {





    // Searching bookings by parameters
    @Query("SELECT b FROM BookingEntity b WHERE " +
            "(:bookingType IS NULL OR b.type = :bookingType) AND " +
            "(:occupied IS NULL OR b.occupied = :occupied) AND " +
            "(:country IS NULL OR b.country = :country) AND " +
            "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND "+
            "(:city IS NULL OR b.city = :city) AND " +
            "(:address IS NULL OR b.address = :address) AND "+
            "(:companyName IS NULL OR b.companyName = :companyName)")
    Page<BookingEntity> findByParameters(@Param("bookingType") BookingType bookingType, @Param("occupied") Boolean occupied,
                                         @Param("country") String country,
                                         @Param("title") String title,
                                         @Param("city") String city,
                                         @Param("address") String address,
                                         @Param("companyName") String companyName,
                                         Pageable pageable
    );
    List<BookingEntity> findBookingEntitiesByCompanyName(String companyName);

    List<BookingEntity> findAllByOrderByIdAsc();
    Optional<BookingEntity> findByTitle(String title);
}
