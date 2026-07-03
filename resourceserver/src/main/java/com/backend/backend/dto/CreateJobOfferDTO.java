package com.backend.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


import java.time.LocalDateTime;

public class CreateJobOfferDTO {

    @NotBlank(message = "Job title is required")
    @Size(min = 3, max = 100, message = "Job title must be between 3 and 100 characters")
    private String jobTitle;

    @NotBlank(message = "Description is required")
    @Size(min = 10, message = "Description must be at least 10 characters long")
    private String description;

    @NotNull(message = "Minimum salary is required")
    @Min(value = 2000, message = "Minimum salary cannot be less than 2000 RON")
    private Integer minSalary;

    @NotNull(message = "Maximum salary is required")
    private Integer maxSalary;

    @NotNull(message = "Application end date is required")
    private LocalDateTime applicationEndDate;

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getMinSalary() { return minSalary; }
    public void setMinSalary(Integer minSalary) { this.minSalary = minSalary; }

    public Integer getMaxSalary() { return maxSalary; }
    public void setMaxSalary(Integer maxSalary) { this.maxSalary = maxSalary; }

    public LocalDateTime getApplicationEndDate() { return applicationEndDate; }
    public void setApplicationEndDate(LocalDateTime applicationEndDate) { this.applicationEndDate = applicationEndDate; }

}
