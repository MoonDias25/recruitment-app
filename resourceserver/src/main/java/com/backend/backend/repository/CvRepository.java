package com.backend.backend.repository;

import com.backend.backend.entity.CV;
import com.backend.backend.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CvRepository extends JpaRepository<CV, String> {

}
