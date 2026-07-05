package com.authserver.server.dto;

import com.authserver.server.enums.PromRequestStatus;

import java.time.LocalDateTime;

public class PromotionRequestDetailDTO {

    private String id;
    private String userId;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private String userRole;
    private String hrNotes;
    private String adminNotes;
    private PromRequestStatus status;
    private LocalDateTime processedAt;

    public PromotionRequestDetailDTO() {}

    public PromotionRequestDetailDTO(String id, String userId, String userFirstName, String userLastName,
                                     String userEmail, String userRole, String hrNotes, String adminNotes,
                                     PromRequestStatus status, LocalDateTime processedAt) {
        this.id = id;
        this.userId = userId;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.userRole = userRole;
        this.hrNotes = hrNotes;
        this.adminNotes = adminNotes;
        this.status = status;
        this.processedAt = processedAt;
    }

    // Getters și Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserFirstName() { return userFirstName; }
    public void setUserFirstName(String userFirstName) { this.userFirstName = userFirstName; }

    public String getUserLastName() { return userLastName; }
    public void setUserLastName(String userLastName) { this.userLastName = userLastName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getHrNotes() { return hrNotes; }
    public void setHrNotes(String hrNotes) { this.hrNotes = hrNotes; }

    public String getAdminNotes() { return adminNotes; }
    public void setAdminNotes(String adminNotes) { this.adminNotes = adminNotes; }

    public PromRequestStatus getStatus() { return status; }
    public void setStatus(PromRequestStatus status) { this.status = status; }

    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }
}
