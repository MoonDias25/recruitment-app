package com.backend.backend.repository;

import com.backend.backend.entity.CandidateApplication;
import com.backend.backend.entity.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateApplicationRepository extends JpaRepository<CandidateApplication, String> {

    boolean existsByUserIdAndJobOfferId(String userId, String jobOfferId);

    List<CandidateApplication> findByUserId(String userId);

    Optional<CandidateApplication> findById(String id);

    List<CandidateApplication> findApplicationsByJobOfferId(String id);

    boolean existsByUserIdAndReviewedBy(String userId, String reviewedBy);
}
