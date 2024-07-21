package com.example.bookingproject.Repository;

import com.example.bookingproject.Config.GrievanceStatus;
import com.example.bookingproject.Model.Grievance;
import com.example.bookingproject.Model.Security.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface GrievanceRepository extends JpaRepository<Grievance,Long> {

    List<Grievance> findAllByStatus(GrievanceStatus status);

    boolean existsByComplainantAndReportedUserAndStatus(UserEntity complainant, UserEntity reportedUser, GrievanceStatus status);

}
