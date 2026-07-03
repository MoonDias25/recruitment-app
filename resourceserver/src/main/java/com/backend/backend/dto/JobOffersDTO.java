package com.backend.backend.dto;

import java.time.LocalDateTime;

public class JobOffersDTO {

    private String id;
    private String jobTitle;
    private String description;
    private Integer minSalary;
    private Integer maxSalary;
    private LocalDateTime applicationEndDate;
    private LocalDateTime creationDate;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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

    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }
}
