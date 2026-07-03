package com.backend.backend.entity;

import com.backend.backend.JobStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="job_offer")
public class JobOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id")
    private String id;

    @Column(name="job_title", nullable = false)
    private String jobTitle;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private JobStatus jobStatus;

    @Column(name="salary_max")
    private int maxSalary;

    @Column(name="salary_min")
    private int minSalary;

    @Lob
    @Column(name="description", columnDefinition = "TEXT")
    private String description;

    @Column(name="application_start")
    private LocalDateTime applicationStartDate;

    @Column(name="application_end")
    private LocalDateTime applicationEndDate;

    @Column(name="reviewed_by")
    private String reviewedBy;

    @JsonManagedReference
    @OneToMany(mappedBy = "jobOffer",
    fetch = FetchType.LAZY,
    cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CandidateApplication> applications;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    public int getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(int maxSalary) {
        this.maxSalary = maxSalary;
    }

    public int getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(int minSalary) {
        this.minSalary = minSalary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getApplicationStartDate() {
        return applicationStartDate;
    }

    public void setApplicationStartDate(LocalDateTime applicationStartDate) {
        this.applicationStartDate = applicationStartDate;
    }

    public LocalDateTime getApplicationEndDate() {
        return applicationEndDate;
    }

    public void setApplicationEndDate(LocalDateTime applicationEndDate) {
        this.applicationEndDate = applicationEndDate;
    }

    public String getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public List<CandidateApplication> getApplications() {
        return applications;
    }

    public void setApplications(List<CandidateApplication> applications) {
        this.applications = applications;
    }
}
