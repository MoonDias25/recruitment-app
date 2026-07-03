package com.backend.backend.repository;

import com.backend.backend.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {

    Optional<UserProfile> findById(String id);

    Optional<UserProfile> findByEmail(String email);

}
