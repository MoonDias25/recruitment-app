package com.backend.backend.dto;

public class PromotionRequestEventDTO {
    private String id;
    private String targetUserId;
    private String hrNotes;

    public PromotionRequestEventDTO() {}

    public PromotionRequestEventDTO(String id, String targetUserId, String hrNotes) {
        this.id = id;
        this.targetUserId = targetUserId;
        this.hrNotes = hrNotes;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTargetUserId() { return targetUserId; }
    public void setTargetUserId(String targetUserId) { this.targetUserId = targetUserId; }

    public String getHrNotes() { return hrNotes; }
    public void setHrNotes(String hrNotes) { this.hrNotes = hrNotes; }
}
