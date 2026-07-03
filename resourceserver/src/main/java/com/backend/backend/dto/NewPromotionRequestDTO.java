package com.backend.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class NewPromotionRequestDTO {

    @NotBlank
    private String targetUserId;
    @NotBlank
    private String hrNotes;

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getHrNotes() {
        return hrNotes;
    }

    public void setHrNotes(String hrNotes) {
        this.hrNotes = hrNotes;
    }
}
