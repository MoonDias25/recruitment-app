package com.backend.backend.dto;

import com.backend.backend.enums.JobStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class JobOfferAdminDTO {

    private String id;

    @NotBlank(message = "Job title cannot be blank")
    private String jobTitle;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Minimum salary is required")
    @Min(value = 0, message = "Minimum salary must be greater than or equal to 0")
    private Integer minSalary;

    @NotNull(message = "Job status is required")
    @NotNull(message = "Maximum salary is required")
    @Min(value = 0, message = "Maximum salary must be greater than or equal to 0")
    private Integer maxSalary;

    @NotNull(message = "Job status is required")
    private JobStatus jobStatus;

    @NotNull(message = "Application start date is required")
    private LocalDateTime applicationStartDate;

    @NotNull(message = "Application end date is required")
    private LocalDateTime applicationEndDate;

    private String reviewedBy;

    public JobOfferAdminDTO() {
    }

    public JobOfferAdminDTO(String id, String jobTitle, String description, Integer minSalary, Integer maxSalary,
                            JobStatus jobStatus, LocalDateTime applicationStartDate, LocalDateTime applicationEndDate,
                            String reviewedBy) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.description = description;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.jobStatus = jobStatus;
        this.applicationStartDate = applicationStartDate;
        this.applicationEndDate = applicationEndDate;
        this.reviewedBy = reviewedBy;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(Integer minSalary) {
        this.minSalary = minSalary;
    }

    public Integer getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(Integer maxSalary) {
        this.maxSalary = maxSalary;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
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
}
