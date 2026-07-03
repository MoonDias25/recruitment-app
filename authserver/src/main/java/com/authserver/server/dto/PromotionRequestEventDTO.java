package com.authserver.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PromotionRequestEventDTO {

    @JsonProperty("id")
    private String id;
    @JsonProperty("targetUserId")
    private String targetUserId;
    @JsonProperty("hrNotes")
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
