package com.backend.backend.repository;

import com.backend.backend.JobStatus;
import com.backend.backend.entity.JobOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, String> {

    List<JobOffer> findByReviewedBy(String id);

    Optional<JobOffer> findJobOfferById(String id);

    @Query("SELECT j FROM JobOffer j WHERE j.jobStatus = JobStatus.ACTIVE AND " +
            "(:title IS NULL OR LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :title, '%')))")
    Page<JobOffer> findActiveJobsWithFilter(@Param("title") String title, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE JobOffer j SET j.jobStatus = :inactiveStatus WHERE j.jobStatus = :activeStatus AND j.applicationEndDate < :now")
    void updateExpiredJobs(
            @Param("now") LocalDateTime now,
            @Param("activeStatus") JobStatus activeStatus,
            @Param("inactiveStatus") JobStatus inactiveStatus
    );
}
