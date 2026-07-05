package com.backend.backend.entity;

import com.backend.backend.enums.ApplicationStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="candidate_application")
public class CandidateApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id")
    private String id;

    @Column(name="user_id")
    private String userId;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private JobOffer jobOffer;

    @Column(name="cv_id")
    private String cvId;

    @Column(name="applied_at")
    private LocalDateTime appliedAt;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private ApplicationStatus candidateStatus;

    @Column(name="reviewed_by")
    private String reviewedBy;

    @Column(name="recruiter_notes")
    private String recruiterNotes;

    @Column(name="reviewed_at")
    private LocalDateTime reviewedAt;


    public CandidateApplication() {
    }

    public CandidateApplication(String userId, JobOffer jobOffer, String cvId, LocalDateTime appliedAt,
                                ApplicationStatus candidateStatus, String reviewedBy) {
        this.userId = userId;
        this.jobOffer = jobOffer;
        this.cvId = cvId;
        this.appliedAt = appliedAt;
        this.candidateStatus = candidateStatus;
        this.reviewedBy = reviewedBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public JobOffer getJobOffer() {
        return jobOffer;
    }

    public void setJobOffer(JobOffer jobOffer) {
        this.jobOffer = jobOffer;
    }

    public String getCvId() {
        return cvId;
    }

    public void setCvId(String cvId) {
        this.cvId = cvId;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    public ApplicationStatus getCandidateStatus() {
        return candidateStatus;
    }

    public void setStatus(ApplicationStatus candidateStatus) {
        this.candidateStatus = candidateStatus;
    }

    public String getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public String getRecruiterNotes() {
        return recruiterNotes;
    }

    public void setRecruiterNotes(String recruiterNotes) {
        this.recruiterNotes = recruiterNotes;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
}
