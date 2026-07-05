package com.backend.backend.dto;

import com.backend.backend.enums.ApplicationStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateJobApplicationDTO {

    String id;

    @NotNull(message = "Status cannot be null")
    ApplicationStatus status;

    String recruiterNotes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public String getRecruiterNotes() {
        return recruiterNotes;
    }

    public void setRecruiterNotes(String recruiterNotes) {
        this.recruiterNotes = recruiterNotes;
    }
}
