package com.example.bookingproject.Service;

import com.example.bookingproject.Model.Grievance;
import com.example.bookingproject.Model.Security.UserEntity;

import java.util.List;

public interface GrievanceService {
    void createGrievence(UserEntity currentUser, Long reportedUserId, String text);

    void resolveGrievance(Long grievanceId, UserEntity currentUser);
    List<Grievance> getPendingGrievances();

    void dismissGrievance(Long grievanceId, UserEntity currentUser);

    boolean hasPendingGrievance(UserEntity complainant, UserEntity reportedUser);
}
