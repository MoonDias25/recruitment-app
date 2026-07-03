package com.backend.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class JobApplicationDTO {

    @NotBlank(message = "Job ID is required")
    private String jobOfferId;

    @NotBlank(message = "CV ID is required")
    private String cvId;

    public String getJobOfferId() { return jobOfferId; }
    public void setJobOfferId(String jobOfferId) { this.jobOfferId = jobOfferId; }

    public String getCvId() { return cvId; }
    public void setCvId(String cvId) { this.cvId = cvId; }
}
