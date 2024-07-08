package com.example.bookingproject.Repository.Security;

import com.example.bookingproject.Model.Security.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity,Long> {
    RoleEntity findByName(String name);

}
